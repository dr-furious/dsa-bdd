public class Main {
    public static void main(String[] args) throws BDDNotInitializedException {
        Tester tester = new Tester();

        // !A!G!KL!N+!A!GK!L!N+!A!GK!LN+!A!GKL!N+!A!GKLN+!AG!KL!N+!AGK!L!N+!AGKL!N+!AGKLN+A!G!K!L!N+A!G!KL!N+A!GK!L!N+A!GKL!N+A!GKLN+AG!K!LN+AGK!LN+AGKLN
        // AGKLN
        // 11001 | 1
        // 11010 | 0

        BinaryDecisionDiagram testBDD = new BinaryDecisionDiagram();
        //testBDD.createBDD("!A!G!KL!N+!A!GK!L!N+!A!GK!LN+!A!GKL!N+!A!GKLN+!AG!KL!N+!AGK!L!N+!AGKL!N+!AGKLN+A!G!K!L!N+A!G!KL!N+A!GK!L!N+A!GKL!N+A!GKLN+AG!K!LN+AGK!LN+AGKLN", "AGKLN");
        //testBDD.printNicely(false);
        //System.out.println(testBDD.useBDD("11001"));
        //System.out.println(Tester.solve("!A!G!KL!N+!A!GK!L!N+!A!GK!LN+!A!GKL!N+!A!GKLN+!AG!KL!N+!AGK!L!N+!AGKL!N+!AGKLN+A!G!K!L!N+A!G!KL!N+A!GK!L!N+A!GKL!N+A!GKLN+AG!K!LN+AGK!LN+AGKLN", "11001"));
        //tester.test(s11, "EBCDA","00001",true);
        //System.out.println("Solve function: " + tester.solve("00001"));

        //1000 | 0
        //1001 | 1
        //1110 | 1
        //1111 | 0

        /*testBDD.createBDD("!E!G!O!V+!E!GOV+!EG!O!V+!EG!OV+!EGOV+E!G!OV+E!GOV+EG!O!V+EG!OV+EGO!V", "EGOV");
        testBDD.printNicely(false);
        System.out.println(testBDD.useBDD("1000"));
        System.out.println(Tester.solve("!E!G!O!V+!E!GOV+!EG!O!V+!EG!OV+!EGOV+E!G!OV+E!GOV+EG!O!V+EG!OV+EGO!V", "1000"));


        String[] arr1 = {"gov","gOV","Gov","GoV", "GOV"};
        String[] arr2 = {"goV", "gOV", "Gov","GoV","GOv"};

        System.out.println(Utility.hash(arr1));
        System.out.println(Utility.hash2(arr1));
        System.out.println();
        System.out.println(Utility.hash(arr2));
        System.out.println(Utility.hash2(arr2));*/

        tester.testBDD(13, false,100);
    }
}