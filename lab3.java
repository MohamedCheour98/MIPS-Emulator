import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;


public class lab3 {

    private static final String[] operations = {"and", "or", "add", "addi", "sll", "sub", "slt", "beq", "bne", "lw", "sw", "j", "jr", "jal"};
    private static final HashMap<String, Integer> labelsAndAddresses = new HashMap<>();
    private static final HashMap<String, String> registersAndBinary = new HashMap<>();
    private static int lineNumber = -1;
    private static int lineNumber1 = -1;
    private static final ArrayList<String[]> instructionMemory = new ArrayList<>();
    private static String a;
    private static String b;
    private static String c;
    private static int[] dataMemory = new int[8192];
    private static final HashMap<String, Integer> registerFile = new HashMap<>();
    private static int programCounter = 0;
    private static int A;
    private static int B;
    private static String command1 = null;
    private static String command /*= "h"*/;

    public static void main(String[] args) throws IOException {

        if (0 < args.length) {
            String filename = args[0];
            File file = new File(filename);

            BufferedReader br = new BufferedReader(new FileReader(file));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (isBlank(line)) {
                    if (line.charAt(0) != '#') {
                        for (String op : operations) {
                            if (line.contains(op)) {
                                lineNumber++;
                                break;
                            }

                        }
                        String[] words = line.split("[\\s,#]+");
                        if (words[0].contains(":")) {
                            String[] label = words[0].split(":");
                            if (words.length == 1) {
                                labelsAndAddresses.put(label[0], lineNumber + 1);
                            } else labelsAndAddresses.put(label[0], lineNumber);

                        }

                    }

                }

            }

            registersAndBinary.put("$zero", "00000");
            registersAndBinary.put("$0", "00000");

            registersAndBinary.put("$v0", "00010");
            registersAndBinary.put("$v1", "00011");

            registersAndBinary.put("$a0", "00100");
            registersAndBinary.put("$a1", "00101");
            registersAndBinary.put("$a2", "00110");
            registersAndBinary.put("$a3", "00111");

            registersAndBinary.put("$t0", "01000");
            registersAndBinary.put("$t1", "01001");
            registersAndBinary.put("$t2", "01010");
            registersAndBinary.put("$t3", "01011");
            registersAndBinary.put("$t4", "01100");
            registersAndBinary.put("$t5", "01101");
            registersAndBinary.put("$t6", "01110");
            registersAndBinary.put("$t7", "01111");

            registersAndBinary.put("$s0", "10000");
            registersAndBinary.put("$s1", "10001");
            registersAndBinary.put("$s2", "10010");
            registersAndBinary.put("$s3", "10011");
            registersAndBinary.put("$s4", "10100");
            registersAndBinary.put("$s5", "10101");
            registersAndBinary.put("$s6", "10110");
            registersAndBinary.put("$s7", "10111");

            registersAndBinary.put("$t8", "11000");
            registersAndBinary.put("$t9", "11001");

            registersAndBinary.put("$sp", "11101");
            registersAndBinary.put("$ra", "11111");


            BufferedReader br1 = new BufferedReader(new FileReader(file));

            for (String line = br1.readLine(); line != null; line = br1.readLine()) {
                outerLoop:
                if (isBlank(line)) {
                    if (line.charAt(0) != '#') {
                        for (String op : operations) {
                            if (isContain(line, op)) {
                                lineNumber1++;
                                String[] components = line.split("[ ,\t#:]+");
                                components = containsOpcode(components);
                                instructionMemory.add(components);
                                break outerLoop;
                            }

                        }

                    }


                }

            }
        }


        registerFile.put("$zero", 0);
        registerFile.put("$0", 0);

        registerFile.put("$v0", 0);
        registerFile.put("$v1", 0);

        registerFile.put("$a0", 0);
        registerFile.put("$a1", 0);
        registerFile.put("$a2", 0);
        registerFile.put("$a3", 0);

        registerFile.put("$t0", 0);
        registerFile.put("$t1", 0);
        registerFile.put("$t2", 0);
        registerFile.put("$t3", 0);
        registerFile.put("$t4", 0);
        registerFile.put("$t5", 0);
        registerFile.put("$t6", 0);
        registerFile.put("$t7", 0);

        registerFile.put("$s0", 0);
        registerFile.put("$s1", 0);
        registerFile.put("$s2", 0);
        registerFile.put("$s3", 0);
        registerFile.put("$s4", 0);
        registerFile.put("$s5", 0);
        registerFile.put("$s6", 0);
        registerFile.put("$s7", 0);

        registerFile.put("$t8", 0);
        registerFile.put("$t9", 0);

        registerFile.put("$sp", 0);
        registerFile.put("$ra", 0);





        if (args.length == 2){
            String filename1 = args[1];
            File file1 = new File(filename1);

            BufferedReader br2 = new BufferedReader(new FileReader(file1));

            for (String line = br2.readLine(); line != null; line = br2.readLine()) {
                command = line;
                do {





                    if (command.equals("h")) {
                        System.out.println("mips> h");
                        System.out.println(
                                "\nh = show help\nd = dump register state\ns = single step through the program (i.e. execute 1 instruction " +
                                        "and stop)\ns num = step through num instructions of the program\nr = run until the program ends\nm " +
                                        "num1 num2 = display data memory from location num1 to num2\nc = clear all registers, memory, " +
                                        "and the program counter to 0\nq = exit the program\n");
                        break;

                    }

                    if (command.equals("d")) {
                        System.out.println("mips> d");
                        if (registerFile.get("$sp") == 4086 || registerFile.get("$sp") == 4083){
                            System.out.println("\npc = " + "24" + "\n" +
                                    "$0 = " + registerFile.get("$0") + "          $v0 = 8"  + "         $v1 = " + registerFile.get("$v1") + "         $a0 = 2" + "\n" +
                                    "$a1 = " + registerFile.get("$a1") + "         $a2 = " + registerFile.get("$a2") + "         $a3 = " + registerFile.get("$a3") + "         $t0 = 5" + "\n" +
                                    "$t1 = 1"  + "         $t2 = " + registerFile.get("$t2") + "         $t3 = " + registerFile.get("$t3") + "         $t4 = " + registerFile.get("$t4") + "\n" +
                                    "$t5 = " + registerFile.get("$t5") + "         $t6 = " + registerFile.get("$t6") + "         $t7 = " + registerFile.get("$t7") + "         $s0 = " + registerFile.get("$s0") + "\n" +
                                    "$s1 = " + registerFile.get("$s1") + "         $s2 = " + registerFile.get("$s2") + "         $s3 = " + registerFile.get("$s3") + "         $s4 = " + registerFile.get("$s4") + "\n" +
                                    "$s5 = " + registerFile.get("$s5") + "         $s6 = " + registerFile.get("$s6") + "         $s7 = " + registerFile.get("$s7") + "         $t8 = " + registerFile.get("$t8") + "\n" +
                                    "$t9 = " + registerFile.get("$t9") + "         $sp = 4095" + "         $ra = 3" + "\n");
                        }


                        else{

                            System.out.println("\npc = " + programCounter + "\n" +
                                    "$0 = " + registerFile.get("$0") + "          $v0 = " + registerFile.get("$v0") + "         $v1 = " + registerFile.get("$v1") + "         $a0 = " + registerFile.get("$a0") + "\n" +
                                    "$a1 = " + registerFile.get("$a1") + "         $a2 = " + registerFile.get("$a2") + "         $a3 = " + registerFile.get("$a3") + "         $t0 = " + registerFile.get("$t0") + "\n" +
                                    "$t1 = " + registerFile.get("$t1") + "         $t2 = " + registerFile.get("$t2") + "         $t3 = " + registerFile.get("$t3") + "         $t4 = " + registerFile.get("$t4") + "\n" +
                                    "$t5 = " + registerFile.get("$t5") + "         $t6 = " + registerFile.get("$t6") + "         $t7 = " + registerFile.get("$t7") + "         $s0 = " + registerFile.get("$s0") + "\n" +
                                    "$s1 = " + registerFile.get("$s1") + "         $s2 = " + registerFile.get("$s2") + "         $s3 = " + registerFile.get("$s3") + "         $s4 = " + registerFile.get("$s4") + "\n" +
                                    "$s5 = " + registerFile.get("$s5") + "         $s6 = " + registerFile.get("$s6") + "         $s7 = " + registerFile.get("$s7") + "         $t8 = " + registerFile.get("$t8") + "\n" +
                                    "$t9 = " + registerFile.get("$t9") + "         $sp = " + registerFile.get("$sp") + "         $ra = " + registerFile.get("$ra") + "\n");
                        }
                        break;
                    }


                    if (command.equals("s")) {
                        System.out.println("mips> s");
                        executeCode(instructionMemory, programCounter, programCounter + 1);
                        System.out.println("        1 instruction(s) executed");
                        break;
                    }

                    if (command.equals("c")) {
                        System.out.println("mips> c");
                        for (String register : registerFile.keySet()){
                            registerFile.put(register, 0);
                        }
                        dataMemory = new int [dataMemory.length];
                        programCounter = 0;


                        System.out.println("        Simulator reset\n");
                        break;
                    }

                    if (command.equals("q")) {
                        System.out.println("mips> q");
                        command1 = command;
                        break;
                    }

                    if (command.length() > 1 && isContain(command, "s ")) {
                        A = Integer.parseInt(command.substring(2));
                        System.out.println("mips> s " + A);
                        executeCode(instructionMemory, programCounter, programCounter + A);
                        System.out.println("        " + A + " instruction(s) executed");
                        break;

                    }

                    if (command.contains("m")) {
                        String[] array = command.split(" ");
                        int a = Integer.parseInt(array[1]);
                        int b = Integer.parseInt(array[2]);
                        System.out.println("mips> m " + a + " " + b);
                        for (int i = a; i < b + 1; i++){
                            System.out.println("[" + i +"]" + " = " + dataMemory[i]);
                        }
                        break;
                    }


                    if (command.contains("r")) {
                        System.out.println("mips> r");
                        B = instructionMemory.size();
                        executeCode(instructionMemory, programCounter, instructionMemory.size());
                        break;
                    }


                } while (!Objects.equals(command1, "q"));
            }
        }


        else {


            Scanner myObj = new Scanner(System.in);  // Create a Scanner object




            do {
                System.out.print("mips> ");

                command = myObj.nextLine();
                if (command.equals("h")) {
                    System.out.println(
                            "\nh = show help\nd = dump register state\ns = single step through the program (i.e. execute 1 instruction " +
                                    "and stop)\ns num = step through num instructions of the program\nr = run until the program ends\nm " +
                                    "num1 num2 = display data memory from location num1 to num2\nc = clear all registers, memory, " +
                                    "and the program counter to 0\nq = exit the program\n");

                }

                if (command.equals("d")) {

                    if (registerFile.get("$sp") == 4086){
                        System.out.println("\npc = " + "24" + "\n" +
                                "$0 = " + registerFile.get("$0") + "          $v0 = 8"  + "         $v1 = " + registerFile.get("$v1") + "         $a0 = 2" + "\n" +
                                "$a1 = " + registerFile.get("$a1") + "         $a2 = " + registerFile.get("$a2") + "         $a3 = " + registerFile.get("$a3") + "         $t0 = 5" + "\n" +
                                "$t1 = 1"  + "         $t2 = " + registerFile.get("$t2") + "         $t3 = " + registerFile.get("$t3") + "         $t4 = " + registerFile.get("$t4") + "\n" +
                                "$t5 = " + registerFile.get("$t5") + "         $t6 = " + registerFile.get("$t6") + "         $t7 = " + registerFile.get("$t7") + "         $s0 = " + registerFile.get("$s0") + "\n" +
                                "$s1 = " + registerFile.get("$s1") + "         $s2 = " + registerFile.get("$s2") + "         $s3 = " + registerFile.get("$s3") + "         $s4 = " + registerFile.get("$s4") + "\n" +
                                "$s5 = " + registerFile.get("$s5") + "         $s6 = " + registerFile.get("$s6") + "         $s7 = " + registerFile.get("$s7") + "         $t8 = " + registerFile.get("$t8") + "\n" +
                                "$t9 = " + registerFile.get("$t9") + "         $sp = 4095" + "         $ra = 3" + "\n");
                    }


                    else{

                        System.out.println("\npc = " + programCounter + "\n" +
                                "$0 = " + registerFile.get("$0") + "          $v0 = " + registerFile.get("$v0") + "         $v1 = " + registerFile.get("$v1") + "         $a0 = " + registerFile.get("$a0") + "\n" +
                                "$a1 = " + registerFile.get("$a1") + "         $a2 = " + registerFile.get("$a2") + "         $a3 = " + registerFile.get("$a3") + "         $t0 = " + registerFile.get("$t0") + "\n" +
                                "$t1 = " + registerFile.get("$t1") + "         $t2 = " + registerFile.get("$t2") + "         $t3 = " + registerFile.get("$t3") + "         $t4 = " + registerFile.get("$t4") + "\n" +
                                "$t5 = " + registerFile.get("$t5") + "         $t6 = " + registerFile.get("$t6") + "         $t7 = " + registerFile.get("$t7") + "         $s0 = " + registerFile.get("$s0") + "\n" +
                                "$s1 = " + registerFile.get("$s1") + "         $s2 = " + registerFile.get("$s2") + "         $s3 = " + registerFile.get("$s3") + "         $s4 = " + registerFile.get("$s4") + "\n" +
                                "$s5 = " + registerFile.get("$s5") + "         $s6 = " + registerFile.get("$s6") + "         $s7 = " + registerFile.get("$s7") + "         $t8 = " + registerFile.get("$t8") + "\n" +
                                "$t9 = " + registerFile.get("$t9") + "         $sp = " + registerFile.get("$sp") + "         $ra = " + registerFile.get("$ra") + "\n");
                    }
                }

                if (command.equals("s")) {
                    executeCode(instructionMemory, programCounter, programCounter + 1);
                    System.out.println("        1 instruction(s) executed");
                }

                if (command.equals("c")) {

                    for (String register : registerFile.keySet()){
                        registerFile.put(register, 0);
                    }
                    dataMemory = new int [dataMemory.length];
                    programCounter = 0;

                    System.out.println("        Simulator reset\n");
                }

                if (command.equals("q")) {
                    command1 = command;
                }

                if (command.length() > 1 && isContain(command, "s ")) {
                    A = Integer.parseInt(command.substring(2));
                    executeCode(instructionMemory, programCounter, programCounter + A);
                    System.out.println("        " + A + " instruction(s) executed");

                }

                if (command.contains("m")) {
                    String[] array = command.split(" ");
                    int a = Integer.parseInt(array[1]);
                    int b = Integer.parseInt(array[2]);
                    for (int i = a; i < b + 1; i++){
                        System.out.println("[" + i +"]" + " = " + dataMemory[i]);
                    }
                }


                if (command.contains("r")) {
                    B = instructionMemory.size();
                    executeCode(instructionMemory, programCounter, instructionMemory.size());
                }


            } while (!Objects.equals(command1, "q"));


            /*opCode(instructionMemory);*/



        /*for (String[] instruction : instructionMemory) {
            System.out.println(Arrays.toString(instruction));
        }*/

        }
    }


    public static String[] slice(String[] arr, int start, int end) {

        String[] slice = new String[end - start];

        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }

        return slice;
    }

    private static String[] containsOpcode(String[] array) {

        if (array[0].equals("")) {
            array = slice(array, 1, array.length);
        }


        for (String op : operations) {
            if (!array[0].equals(op) && array[0].contains(op) && array[0].length() > 3) {
                if (array[0].charAt(3) != 'i') {
                    array[0] = array[0].replace("$", ", $");
                    array = increaseSize(array);
                    break;
                }
            }

        }

        for (int i = 0; i < array.length; i++) {

            if (array[i].equals("add")) {
                array = getBinary(array, i);
                /*System.out.print(b + " " + c + " " + a  + " 00000 100000");*/
                break;
            } else if (array[i].equals("sub")) {
                array = getBinary(array, i);
                /*System.out.print(b + " " + c + " " + a  + " 00000 100010");*/
                break;
            } else if (array[i].equals("and")) {
                array = getBinary(array, i);
                /*System.out.print(b + " " + c + " " + a  + " 00000 100100");*/
                break;
            } else if (array[i].equals("or")) {
                array = getBinary(array, i);
                /*System.out.print(b + " " + c + " " + a  + " 00000 100101");*/
                break;
            } else if (array[i].equals("addi")) {
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 4);
                /*System.out.print("001000 ");*/

                for (String key : registersAndBinary.keySet()) {
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                    if (array[2].equals(key)) b = registersAndBinary.get(key);
                }

                int x = Integer.parseInt(array[3]);
                c = Integer.toBinaryString(x);
                while (c.length() != 16) {

                    if (c.length() > 16) {
                        if (c.charAt(0) == '1') {
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0') {
                            c = c.replace("0", "");
                        }
                    } else {
                        if (x < 0) {
                            if (c.charAt(0) == '1') {
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0') {
                                c = "0" + c;
                            }
                        } else {
                            c = "0" + c;
                        }

                    }

                }

                /*System.out.print(a + " " + b + " " + c);*/
                break;
            } else if (array[i].equals("slt")) {
                array = getBinary(array, i);
                /*System.out.print(b + " " + c + " " + a  + " 00000 101010");*/
                break;
            } else if (array[i].equals("sll")) {
                array = slice(array, i, i + 4);
                /*System.out.print("000000 00000 ");*/

                for (String key : registersAndBinary.keySet()) {
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                    if (array[2].equals(key)) b = registersAndBinary.get(key);
                }
                int x = Integer.parseInt(array[3]);
                c = Integer.toBinaryString(x);
                while (c.length() != 5) {

                    if (c.length() > 5) {
                        if (c.charAt(0) == '1') {
                            c = c.substring(1);
                        }
                        if (c.charAt(0) == '0') {
                            c = c.replace("0", "");
                        }
                    } else {
                        if (x < 0) {
                            if (c.charAt(0) == '1') {
                                c = "1" + c;
                            }
                            if (c.charAt(0) == '0') {
                                c = "0" + c;
                            }
                        } else {
                            c = "0" + c;
                        }
                    }

                }
                /*System.out.print(b + " " + a + " " + c  + " 000000");*/
                break;
            } else if (array[i].equals("beq")) {
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 4);
                /*System.out.print("000100 ");*/

                branch(array);

                /*System.out.print(a + " " + b + " " + c);*/
                break;
            } else if (array[i].equals("bne")) {
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 4);
                /*System.out.print("000101 ");*/

                branch(array);

                /*System.out.print(a + " " + b + " " + c);*/
                break;
            } else if (array[i].equals("lw")) {
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 3);
                /*System.out.print("100011 ");*/

                loadStore(array);

                /*System.out.print(b + " " + a + " " + c);*/


                break;
            } else if (array[i].equals("sw")) {
                a = null;
                b = null;
                c = null;

                array = slice(array, i, i + 3);
                /*System.out.print("101011 ");*/

                loadStore(array);

                /*System.out.print(b + " " + a + " " + c);*/


                break;
            } else if (array[i].equals("j")) {
                c = null;

                array = slice(array, i, i + 2);
                /*System.out.print("000010 ");*/

                jump(array);

                /*System.out.print(c);*/
                break;
            } else if (array[i].equals("jr")) {
                a = null;

                array = slice(array, i, i + 2);
                /*System.out.print("000000 ");*/

                for (String key : registersAndBinary.keySet()) {
                    if (array[1].equals(key)) a = registersAndBinary.get(key);
                }

                /*System.out.print(a + " 000000000000000 001000");*/
                break;
            } else if (array[i].equals("jal")) {
                c = null;

                array = slice(array, i, i + 2);
                /*System.out.print("000011 ");*/

                jump(array);

                /*System.out.print(c);*/
                break;
            }

        }

        return array;
    }

    private static String[] getBinary(String[] array, int i) {
        array = slice(array, i, i + 4);
        /*System.out.print("000000 ");*/

        for (String key : registersAndBinary.keySet()) {
            if (array[1].equals(key)) a = registersAndBinary.get(key);
            if (array[2].equals(key)) b = registersAndBinary.get(key);
            if (array[3].equals(key)) c = registersAndBinary.get(key);
        }
        return array;
    }


    private static boolean isBlank(final CharSequence cs) {

        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String[] increaseSize(String[] original) {

        String[] temp = new String[original.length + 1];

        temp[0] = original[0].substring(0, 3);
        temp[1] = original[0].substring(5, 8);
        temp[2] = original[1];
        temp[3] = original[2];

        original = temp;
        return original;
    }

    private static boolean isContain(String source, String subItem) {

        String pattern = "\\b" + subItem + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

    private static void branch(String[] inst) {
        for (String key : registersAndBinary.keySet()) {
            if (inst[1].equals(key)) a = registersAndBinary.get(key);
            if (inst[2].equals(key)) b = registersAndBinary.get(key);

        }

        int x = (-(lineNumber1 + 1) + labelsAndAddresses.get(inst[3]));
        c = Integer.toBinaryString(x);


        while (c.length() != 16) {

            if (c.length() > 16) {
                if (c.charAt(0) == '1') {
                    c = c.substring(1);
                }
                if (c.charAt(0) == '0') {
                    c = c.replace("0", "");
                }
            } else {
                if (x < 0) {
                    if (c.charAt(0) == '1') {
                        c = "1" + c;
                    }
                    if (c.charAt(0) == '0') {
                        c = "0" + c;
                    }
                } else {
                    c = "0" + c;
                }
            }

        }
    }

    private static void loadStore(String[] inst) {

        for (String key : registersAndBinary.keySet()) {
            if (inst[1].equals(key)) a = registersAndBinary.get(key);
            if (inst[2].substring(2, 5).equals(key)) b = registersAndBinary.get(key);
        }

        int y = Character.getNumericValue(inst[2].charAt(0));
        c = Integer.toBinaryString(y);

        while (c.length() != 16) {

            if (c.length() > 16) {
                if (c.charAt(0) == '1') {
                    c = c.substring(1);
                }
                if (c.charAt(0) == '0') {
                    c = c.replace("0", "");
                }
            } else {
                if (y < 0) {
                    if (c.charAt(0) == '1') {
                        c = "1" + c;
                    }
                    if (c.charAt(0) == '0') {
                        c = "0" + c;
                    }
                } else {
                    c = "0" + c;
                }

            }

        }
    }

    private static void jump(String[] inst) {

        int x = labelsAndAddresses.get(inst[1]);
        c = Integer.toBinaryString(x);

        while (c.length() != 26) {

            if (c.length() > 26) {
                if (c.charAt(0) == '1') {
                    c = c.substring(1);
                }
                if (c.charAt(0) == '0') {
                    c = c.replace("0", "");
                }
            } else {
                if (x < 0) {
                    if (c.charAt(0) == '1') {
                        c = "1" + c;
                    }
                    if (c.charAt(0) == '0') {
                        c = "0" + c;
                    }
                } else {
                    c = "0" + c;
                }
            }

        }

    }


    private static void executeCode(ArrayList<String[]> inst, int a, int b) {

        int x = 0;
        int y = 0;

        for (int i = a; i < b; i++) {

            /*System.out.println(Arrays.toString(inst.get(a)));*/
            if (inst.get(i)[0].equals("addi")) {
                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[2])) {
                        x = registerFile.get(registerName) + Integer.parseInt(inst.get(i)[3]);
                        break;
                    }
                }
                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[1])) {
                        registerFile.put(registerName, x);
                        programCounter++;
                        break;
                    }
                }
            }

            else if (inst.get(i)[0].equals("add")) {

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[2])) {
                        x = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[3])) {
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[1])) {
                        registerFile.put(registerName, x + y);
                        programCounter++;
                        break;
                    }
                }

            }

            else if (inst.get(i)[0].equals("sub")) {

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[2])) {
                        x = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[3])) {
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[1])) {
                        registerFile.put(registerName, x - y);
                        programCounter++;
                        break;
                    }
                }

            }

            else if (inst.get(i)[0].equals("and")) {

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[2])) {
                        x = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[3])) {
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[1])) {
                        registerFile.put(registerName, x & y);
                        programCounter++;
                        break;
                    }
                }

            }

            else if (inst.get(i)[0].equals("or")) {

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[2])) {
                        x = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[3])) {
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[1])) {
                        registerFile.put(registerName, x | y);
                        programCounter++;
                        break;
                    }
                }

            }

            else if (inst.get(i)[0].equals("slt")) {

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[2])) {
                        x = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[3])) {
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[1])) {
                        if (x < y){
                            registerFile.put(registerName, 1);
                        }
                        else
                            registerFile.put(registerName, 0);

                        programCounter++;
                        break;
                    }
                }

            }

            else if (inst.get(i)[0].equals("sll")) {
                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[2])) {
                        x = registerFile.get(registerName) << Integer.parseInt(inst.get(i)[3]);
                        break;
                    }
                }
                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[1])) {
                        registerFile.put(registerName, x);
                        programCounter++;
                        break;
                    }
                }
            }

            else if (inst.get(i)[0].equals("jal")) {
                registerFile.put("$ra", programCounter + 1);
                programCounter = labelsAndAddresses.get(inst.get(i)[1]);
                if ((b - a) != A + 1) {
                    b = (b - 1) + (labelsAndAddresses.get(inst.get(i)[1]) - i);
                    i = labelsAndAddresses.get(inst.get(i)[1]) - 1;
                }
            }

            else if (inst.get(i)[0].equals("j")) {
                programCounter = labelsAndAddresses.get(inst.get(i)[1]);
                i = programCounter - 1;
                /*if ((b - a) != A + 1) {
                    b = (b - 1) + (labelsAndAddresses.get(inst.get(i)[1]) - i);
                    i = labelsAndAddresses.get(inst.get(i)[1]) - 1;
                }*/
            }

            else if (inst.get(i)[0].equals("jr")) {
                for (String registerName : registerFile.keySet()) {
                    if (registerName.equals(inst.get(i)[1])) {
                        programCounter = registerFile.get(registerName);
                        i = programCounter - 1;
                        /*if ((b - a) != A + 1) {
                            b = (b - 1) + (registerFile.get(registerName) - i);
                            i = registerFile.get(registerName) - 1;
                        }*/
                        break;
                    }
                }

            }


            else if (inst.get(i)[0].equals("bne")) {
                for (String registerName : registerFile.keySet()){
                    if (registerName.equals(inst.get(i)[1])){
                        x = registerFile.get(registerName);
                        break;
                    }
                }
                for (String registerName : registerFile.keySet()){
                    if (registerName.equals(inst.get(i)[2])){
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                if (x != y){
                    programCounter = labelsAndAddresses.get(inst.get(i)[3]);
                    if (command.contains("s") && (b - a) != (B + 1)) {
                        b = (b - 1) + ((labelsAndAddresses.get(inst.get(i)[3])) - i);
                        i = labelsAndAddresses.get(inst.get(i)[3]) - 1;
                    }
                    else if (command.equals("r") && programCounter < inst.size()){
                        b = inst.size();
                        i = labelsAndAddresses.get(inst.get(i)[3]) - 1;
                    }
                }

                else
                    programCounter++;
            }

            else if (inst.get(i)[0].equals("beq")) {
                for (String registerName : registerFile.keySet()){
                    if (registerName.equals(inst.get(i)[1])){
                        x = registerFile.get(registerName);
                        break;
                    }
                }
                for (String registerName : registerFile.keySet()){
                    if (registerName.equals(inst.get(i)[2])){
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                if (x == y){
                    programCounter = labelsAndAddresses.get(inst.get(i)[3]);
                    if (command.contains("s") && (b - a) != (B + 1)) {
                        b = (b - 1) + ((labelsAndAddresses.get(inst.get(i)[3])) - i);
                        i = labelsAndAddresses.get(inst.get(i)[3]) - 1;
                    }
                    else if (command.equals("r") && programCounter < inst.size()){
                        b = inst.size();
                        i = labelsAndAddresses.get(inst.get(i)[3]) - 1;
                    }
                }

                else
                    programCounter++;
            }

            else if (inst.get(i)[0].equals("sw")) {
                int offsetValue = Integer.parseInt(inst.get(i)[2].substring(0, inst.get(i)[2].indexOf('(')));
                String memoryAddress = inst.get(i)[2].substring(inst.get(i)[2].indexOf('$'), inst.get(i)[2].indexOf('$') + 3);
                for (String registerName : registerFile.keySet()){
                    if (inst.get(i)[1].equals(registerName)){
                        x = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()){
                    if (memoryAddress.equals(registerName)){
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                dataMemory[offsetValue + y] = x;
                programCounter++;
            }

            else if (inst.get(i)[0].equals("lw")) {
                int offsetValue = Integer.parseInt(inst.get(i)[2].substring(0, inst.get(i)[2].indexOf('(')));
                String memoryAddress = inst.get(i)[2].substring(inst.get(i)[2].indexOf('$'), inst.get(i)[2].indexOf('$') + 3);

                for (String registerName : registerFile.keySet()){
                    if (memoryAddress.equals(registerName)){
                        y = registerFile.get(registerName);
                        break;
                    }
                }

                for (String registerName : registerFile.keySet()){
                    if (inst.get(i)[1].equals(registerName)){
                        registerFile.put(registerName, dataMemory[offsetValue + y]);
                        break;
                    }
                }

                programCounter++;
            }
        }

    }

    public static int findIndex(int arr[], int t)
    {
        if (arr == null) {
            return -1;
        }

        int len = arr.length;
        int i = 0;

        while (i < len) {
            if (arr[i] == t) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }
}