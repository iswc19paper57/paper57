package alg;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.*;
import beans.triple;

public class Evaluations{
    Connection con = new DBconnecter().conn;

    // Coverage of Query Keywords
    public double coKyw(Set<triple> snip, Set<String> keywords) {

        if(snip == null)return 0;
        Set<String>snipWords = new HashSet<>();
        int count = 0;
        try {
            for(triple iter: snip) {
                String s = "";
                if(iter.getS() != null)s += StringUtil.splitLabel(iter.getS())+" ";
                if(iter.getP() != null)s += StringUtil.splitLabel(iter.getP())+" ";
                if(iter.getO() != null)s += StringUtil.splitLabel(iter.getO())+" ";
                if(s != null) {
                    String[] kws = s.split(" ");
                    for(String i: kws) {
                        snipWords.add(i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String iter: keywords) {
            if(snipWords.contains(iter)) {
                count++;
            }
        }
        return (double)count/keywords.size();
    }

    //Coverage of Connections between Query Keywords
    public double coCnx(Set<triple> snip, Set<String> keywords, int dataset_id) {

        if(snip == null)return 0;
        Map<triple, Set<triple>>neighbor = new HashMap<>();
        HashSet<Integer> lite = new HashSet<>();
        try{
            Statement st = con.createStatement();
            ResultSet r = st.executeQuery("select id from uri_label_id where " +
                    "is_literal = 1 and dataset_local_id = "+dataset_id);
            while(r.next()){
                lite.add(r.getInt(1));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        for(triple iter: snip) {
            Set<triple>temp = new HashSet<>();
            int sid = iter.getsid();
            int oid = iter.getoid();
            for(triple t: snip) {
                if(sid != 0 && (t.getsid() == sid||t.getoid() == sid)) {
                    temp.add(t);
                }
                if(oid != 0 && (t.getsid() == oid||t.getoid() == oid)){
                    temp.add(t);
                }
            }
            neighbor.put(iter, temp);
        }
        Map<String, Set<triple>>kw2trp = new HashMap<>();
        try {
            for(triple iter: snip) {
                String s = "";
                if(iter.getS() != null)s += StringUtil.splitLabel(iter.getS())+" ";
                if(iter.getP() != null)s += StringUtil.splitLabel(iter.getP())+" ";
                if(iter.getO() != null)s += StringUtil.splitLabel(iter.getO())+" ";
                Set<String>temp = new HashSet<>();
                if(s != null) {
                    String[] splitS = s.split(" ");
                    for(String i: splitS) {
                        temp.add(i);
                    }
                    for(String st: temp) {
                        if(keywords.contains(st)) {
                            if(kw2trp.containsKey(st)) {
                                Set<triple> tempset = kw2trp.get(st);
                                tempset.add(iter);
                                kw2trp.put(st, tempset);
                            }
                            else {
                                Set<triple> tempset = new HashSet<>();
                                tempset.add(iter);
                                kw2trp.put(st, tempset);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = 0;
        List<String> kws = new ArrayList<>();
        for(String iter: keywords) {
            kws.add(iter);
        }
        Set<triple>checked;
        Set<triple>nbs;
        for(int i = 0; i < kws.size(); i++) {
            String start = kws.get(i);
            for(int j = i+1; j < kws.size(); j++) {
                String end = kws.get(j);
                nbs = new HashSet<>();
                if(!kw2trp.containsKey(start) || !kw2trp.containsKey(end))continue;
                for(triple iter0: kw2trp.get(start)){
                    nbs.addAll(neighbor.get(iter0));
                }
                checked = new HashSet<>();
                int flag = 0;
                while(!nbs.isEmpty()) {
                    Set<triple>newnb = new HashSet<>();
                    Set<triple>del = new HashSet<>();
                    for(triple iter: nbs) {
                        if(kw2trp.get(end).contains(iter)) {
                            count ++;
                            flag = 1;
                            break;
                        }
                        else {
                            del.add(iter);
                            checked.add(iter);
                            for(triple t: neighbor.get(iter)) {
                                if(!checked.contains(t) && !nbs.contains(t)) {
                                    newnb.add(t);
                                }
                            }
                        }
                    }
                    if(flag == 1)break;
                    nbs.addAll(newnb);
                    for(triple d: del) {
                        nbs.remove(d);
                    }
                }
                if(flag == 1)continue;
            }
        }
        int kwsize = keywords.size();
        if(kwsize == 1)return coKyw(snip, keywords);
        return (double)(2*count)/(kwsize*(kwsize-1));
    }

    public double coSkm(Set<triple>snip, int dataset_id) {

        if(snip == null)return 0;
        Set<Integer>snipPrp = new HashSet<>();
        Set<Integer>snipCls = new HashSet<>();
        double ttlTriple = 0, ttlCls = 0;
        double frqCls = 0, frqPrp = 0;

        for(triple iter: snip) {
            if(iter.getP() != null) {
                snipPrp.add(iter.getpid());
                if(iter.getP().equals("type")) {
                    snipCls.add(iter.getoid());
                }
            }
        }
        try {
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery("select triple_count from dataset_info where "
                    + "dataset_local_id = "+dataset_id);
            if(rs.next()) {
                ttlTriple = rs.getInt(1);
            }
            int typeID = 0;
            rs = state.executeQuery("select id from uri_label_id where dataset_local_id "
                    + "= "+dataset_id+" and uri = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#type'");
            if(rs.next()) {
                typeID = rs.getInt("id");
            }
            rs = state.executeQuery("select count(*) from triple where "
                    + "dataset_local_id = "+dataset_id+" and predicate = "+typeID);
            if(rs.next()) {
                ttlCls = rs.getInt(1);
            }

            for(int iter: snipPrp) {
                rs = state.executeQuery("select count(*) from triple where "
                        + "dataset_local_id = "+dataset_id+" and predicate = "+iter);
                if(rs.next()) {
                    frqPrp += rs.getInt(1);
                }
            }
            for(int iter: snipCls) {
                rs = state.executeQuery("select count(*) from triple where "
                        + "(dataset_local_id = "+dataset_id+" and predicate = "+typeID+" and object = "+iter+")");
                if(rs.next()) {
                    frqCls += rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ttlTriple != 0)frqPrp = (double)frqPrp/ttlTriple;
        if(ttlCls != 0)frqCls = (double)frqCls/ttlCls;

        if(frqPrp+frqCls != 0)return 2*frqPrp*frqCls/(frqPrp+frqCls);
        else return 0;
    }

    //Coverage of Data
    public double coDat(Set<triple> snip, int dataset_id) {

        if(snip == null)return 0;

        Set<Integer> Ent = new HashSet<>();
        Set<Integer>literal = new HashSet<>();
        try {
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery("select id from uri_label_id where "
                    + "dataset_local_id = "+dataset_id+" and is_literal = 1");
            while(rs.next()) {
                literal.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(triple iter:snip) {

            if(iter.getS()!=null && !literal.contains(iter.getsid())){
                Ent.add(iter.getsid());
            }
            if(iter.getP()!=null && !iter.getP().equals("type") && iter.getO()!=null && !literal.contains(iter.getoid())) {
                Ent.add(iter.getoid());
            }
        }
        double inTotal = 0;
        double outTotal = 0;
        int maxout = 0, maxin = 0;
        if(Ent.isEmpty())return 0;
        try {
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery("select max_out_degree, max_in_degree from dataset_info where dataset_local_id = "+dataset_id);
            if(rs.next()) {
                maxout = rs.getInt("max_out_degree");
                maxin = rs.getInt("max_in_degree");
            }
            int count;
            for(int iter: Ent) {
                rs = state.executeQuery("select count(*) from triple where "
                        + "dataset_local_id ="+dataset_id+" and subject = "+iter);
                count = 0;
                if(rs.next()) {
                    count = rs.getInt(1);
                }
                outTotal += Math.log((double)count +1);
                rs = state.executeQuery("select count(*) from triple where "
                        + "dataset_local_id ="+dataset_id+" and object = "+iter);
                if(rs.next()) {
                    count = rs.getInt(1);
                }
                inTotal += Math.log((double)count +1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(maxout == 0) inTotal = 0;
        else inTotal = inTotal/(Ent.size() * Math.log((double)maxout +1));
        if(maxin == 0) outTotal = 0;
        else outTotal = outTotal/(Ent.size() * Math.log((double)maxin +1));
        double coDat = 0;
        if(inTotal + outTotal != 0) {
            coDat = 2*(inTotal * outTotal)/(inTotal + outTotal);
        }
        return coDat;
    }
}

