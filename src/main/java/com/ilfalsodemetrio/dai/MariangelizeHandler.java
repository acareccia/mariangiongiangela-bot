package com.ilfalsodemetrio.dai;

import java.util.StringTokenizer;

/**
 * Created by lbrtz on 08/09/16.
 */
public class MariangelizeHandler {
    /**
     *
     * @param text
     * @return
     */
    public static String process(String text) {
        StringTokenizer st = new StringTokenizer(text);
        StringBuilder stringBuilder = new StringBuilder();
        boolean doppio = false;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (token.length() >= 6 && !doppio) {
                if (token.endsWith("a")) {
                    stringBuilder.append("cosa");
                    doppio = true;
                } else if (token.endsWith("o")) {
                    stringBuilder.append("coso");
                    doppio = true;
                } else if (token.endsWith("e")) {
                    stringBuilder.append("cose");
                    doppio = true;
                } else if (token.endsWith("i")) {
                    stringBuilder.append("cosi");
                    doppio = true;
                } else {
                    stringBuilder.append(token);
                    doppio = false;
                }
            } else {
                stringBuilder.append(token);
                doppio = false;
            }

            stringBuilder.append(" ");
        }


        return stringBuilder.toString();
    }
}
