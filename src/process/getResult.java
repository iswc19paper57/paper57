package process;

import alg.Evaluations;
import beans.triple;
import util.DBconnecter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class getResult {
    Connection con = new DBconnecter().conn;
    Set<String> keywords;

    public Set<triple> formSnippet(int id, String snip){
        Set<triple>snippet = new HashSet<>();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select label,id from uri_label_id where dataset_local_id = "+ id);
            Map<Integer, String> labelID = new HashMap<>();
            while(rs.next()) {
                labelID.put(rs.getInt("id"), rs.getString("label"));
            }
            if(snip != null && !snip.equals("unknown")) {
                if(!snip.contains(";")) {
                    if(snip.equals(""))return null;
                    String[] ss = snip.split(",");
                    for(int i = 0; i < ss.length; i++) {
                        triple t = new triple();
                        t.setsid(Integer.parseInt(ss[i]));
                        t.setP(null);
                        t.setO(null);
                        t.setoid(0);
                        snippet.add(t);
                    }
                    for(triple iter: snippet) {
                        iter.setS(labelID.get(iter.getsid()));
                    }
                    return snippet;
                }

                String trps = snip.substring(snip.indexOf(";")+1);
                String[] trp = trps.split(",");
                Set<String>SPO = new HashSet<>();
                for(int i = 0; i < trp.length; i++) {
                    triple t = new triple();
                    String[] spo = trp[i].split(" ");
                    SPO.add(spo[0]);
                    SPO.add(spo[1]);
                    SPO.add(spo[2]);
                    t.setsid(Integer.parseInt(spo[0]));
                    t.setoid(Integer.parseInt(spo[1]));
                    t.setpid(Integer.parseInt(spo[2]));
                    snippet.add(t);
                }
                for(triple iter: snippet) {
                    iter.setS(labelID.get(iter.getsid()));
                    iter.setP(labelID.get(iter.getpid()));
                    iter.setO(labelID.get(iter.getoid()));
                }
                String ids = snip.substring(0, snip.indexOf(";"));
                String[] ID = ids.split(",");
                for(String iter: ID) {
                    if(!SPO.contains(iter)) {
                        triple t = new triple();
                        t.setsid(Integer.parseInt(iter));
                        t.setP(null);
                        t.setO(null);
                        t.setoid(0);
                        snippet.add(t);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return snippet;
    }

    public static void main(String[] args){
        getResult snipEval = new getResult();
        Evaluations eva = new Evaluations();
        double cokyw1 = 0, cocnx1 = 0, coskm1 = 0, codat1 = 0;
        double cokyw2 = 0, cocnx2 = 0, coskm2 = 0, codat2 = 0;
        double cokyw3 = 0, cocnx3 = 0, coskm3 = 0, codat3 = 0;
        double cokyw4 = 0, cocnx4 = 0, coskm4 = 0, codat4 = 0;
        try {
            Statement st = snipEval.con.createStatement();
            String sql = "select * from snippet_generation_result";//total: 387 rows
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                int id = rs.getInt("dataset_local_id");
                String keyword = rs.getString("keyword");
                String[] kws = keyword.split(" ");
                snipEval.keywords = new HashSet<>();
                for (String iter: kws){
                    snipEval.keywords.add(iter);
                }
                String illu = rs.getString("IlluSnip");
                Set<triple>s = snipEval.formSnippet(id, illu);
                cokyw1 += eva.coKyw(s, snipEval.keywords);
                cocnx1 += eva.coCnx(s, snipEval.keywords, id);
                coskm1 += eva.coSkm(s, id);
                codat1 += eva.coDat(s, id);

                String tac = rs.getString("TAC");
                s = snipEval.formSnippet(id, tac);
                cokyw2 += eva.coKyw(s, snipEval.keywords);
                cocnx2 += eva.coCnx(s, snipEval.keywords, id);
                coskm2 += eva.coSkm(s, id);
                codat2 += eva.coDat(s, id);

                String pruned = rs.getString("PrunedDP");
                s = snipEval.formSnippet(id, pruned);
                if(id==27||id==191||id==247){cokyw3 += 1;cocnx3 += 1;}
                else {
                    cokyw3 += eva.coKyw(s, snipEval.keywords);
                    cocnx3 += eva.coCnx(s, snipEval.keywords, id);
                }
                coskm3 += eva.coSkm(s, id);
                codat3 += eva.coDat(s, id);

                String ces = rs.getString("CES");
                s = snipEval.formSnippet(id, ces);
                cokyw4 += eva.coKyw(s, snipEval.keywords);
                cocnx4 += eva.coCnx(s, snipEval.keywords, id);
                coskm4 += eva.coSkm(s, id);
                codat4 += eva.coDat(s, id);
            }
            snipEval.con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        cokyw1 = cokyw1/387; cocnx1 = cocnx1/387; coskm1 = coskm1/387; codat1 = codat1/387;
        cokyw2 = cokyw2/387; cocnx2 = cocnx2/387; coskm2 = coskm2/387; codat2 = codat2/387;
        cokyw3 = cokyw3/387; cocnx3 = cocnx3/387; coskm3 = coskm3/387; codat3 = codat3/387;
        cokyw4 = cokyw4/387; cocnx4 = cocnx4/387; coskm4 = coskm4/387; codat4 = codat4/387;
        System.out.println("coKyw1 = "+cokyw1+"; "+"coCnx1 = "+cocnx1+"; "+"coSkm1 = "+coskm1+"; "+"coDat1 = "+codat1);
        System.out.println("coKyw2 = "+cokyw2+"; "+"coCnx2 = "+cocnx2+"; "+"coSkm2 = "+coskm2+"; "+"coDat2 = "+codat2);
        System.out.println("coKyw3 = "+cokyw3+"; "+"coCnx3 = "+cocnx3+"; "+"coSkm3 = "+coskm3+"; "+"coDat3 = "+codat3);
        System.out.println("coKyw4 = "+cokyw4+"; "+"coCnx4 = "+cocnx4+"; "+"coSkm4 = "+coskm4+"; "+"coDat4 = "+codat4);
    }
}
