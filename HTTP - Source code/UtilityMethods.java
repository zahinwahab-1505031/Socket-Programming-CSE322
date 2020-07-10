public class UtilityMethods {

    //bunch of utility methods, pretty self-explanatory

    public static String createErrorBody(String responseCode, String message)
    {
        StringBuilder body = new StringBuilder();
        body.append("<HTML>\r\n");
        body.append("<HEAD><TITLE>"+message+"</TITLE></HEAD>\r\n");
        body.append("<BODY><H1>"+responseCode+": "+message+"</H1></BODY>\r\n");
        body.append("</HTML>\r\n");
        return body.toString();
    }

    public static  String createPostBody(String name)
    {
        String body = "<html>\r\n" +
                "\t<head>\r\n" +
                "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "\t</head>\r\n" +
                "\t<body>\r\n" +
                "\t\t<h1> Welcome to CSE 322 Offline 1</h1>\r\n" +
                "\t\t<h2> HTTP REQUEST TYPE-> POST</h2>\r\n" +
                "\t\t<h2> Post->"+name+ "</h2>\r\n" +
                "\t\t<form name=\"input\" action=\"http://localhost:9999/c.html\" method=\"post\">\r\n" +
                "\t\tYour Name: <input type=\"text\" name=\"user\">\r\n" +
                "\t\t<input type=\"submit\" value=\"Submit\">\r\n" +
                "\t\t</form>\r\n" +
                "\t</body>\r\n" +
                "</html>";
        return body;
    }
}
