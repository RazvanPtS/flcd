public class Main {

    public static void main(String[] args) {
        SymbolTable st =new SymbolTable();
        st.add("hP");
        st.add("i2");
        st.add("i1");
        st.add("i11");
        System.out.println(st.get("i1"));
        //st.remove("i1");
    }
}
