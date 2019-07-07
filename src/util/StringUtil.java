package util;

import org.tartarus.snowball.ext.PorterStemmer;

public class StringUtil {
    public static String splitLabel(String label){
        String keyword = label.replaceAll("\"|#", "");
        keyword = keyword.replaceAll("\\.|'|_|-|\\\\|/|\\(|\\)|\\.\\.\\.|:|,|;|\\?|!|\\+|~|&|\\$|%|\\^|@|\\*|=|<|>|\\[|\\]|\\{|\\}|\\|", " ");
        keyword = keyword.replaceAll("u00..", " ").trim();
        String[] labelSplit = keyword.split("\\s+");
        if(labelSplit.length == 0)
            return "";
        else if(labelSplit.length == 1){
            labelSplit = keyword.split("(?<!^)(?=[A-Z])"); //dealWithCasesLikeThis
        }
        StringBuilder sb = new StringBuilder();
        for(String ls:labelSplit){
            PorterStemmer stemmer = new PorterStemmer();
            stemmer.setCurrent(ls.toLowerCase());
            stemmer.stem();
            sb.append(stemmer.getCurrent()+" ");
        }
        keyword = sb.toString().trim();
        return keyword;
    }
}
