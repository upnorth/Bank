/*
2 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
/**
 *
 * @author Karl
 */
public class Bank {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.visit();
    }
    void visit() {
        Scanner scanner = new Scanner(System.in);
        bankManager bankmanager = new bankManager();
        boolean visiting = true;
        while(visiting){
            System.out.println("Välkommen till banken!");
            System.out.println("1: Logga in. 0: Lämna banken");
            int input = scanner.nextInt();
            switch(input){
                case 0:
                    visiting = false;
                    break;
                case 1:
                    bankmanager.login();
                    break;
                default:
                    break;
            }
        }
    }
    public class bankManager {
        Scanner scanner = new Scanner(System.in);
        private static final String ADMIN_VALUE_OPTION = "admin";
        private int nästaKonto = 0;
        public final ArrayList<Kund> kunder = new ArrayList<>();

        private boolean kundRedanRegistrerad(Kund k) {
            int[] nyKund = k.getPnr();
            boolean fannsRedan = false;

            for (Kund kund : kunder) {
                if (kund.getPnr() == nyKund) {
                    fannsRedan = true;
                } 
            }
            return fannsRedan;
        }
        public void login(){
            System.out.println("Ange kundnummer:");
            
        	String personNummer = scanner.next();
        	
        	if (personNummer.equals(ADMIN_VALUE_OPTION)){
        		this.kontroll(kunder);
        		return;		
        	} //kolla om personNummer sträng har samma värde som ADMIN_VALUE_OPTION
        	
        	int[] persNrAsIntArray = convertStringToIntArray(personNummer);

            if(Luhn(persNrAsIntArray)){
                for(Kund kund : kunder){
                    if(Arrays.equals(kund.personnummer, persNrAsIntArray)){
                        System.out.println("Du är identifierad, välkommen in!");
                        this.kundCenter(kund);
                        return;
                    }
                }
                
                System.out.println("Du är inte en registrerad kund i denna bank.");
                return;
            }
            else{
                System.out.println("Inte ett giltigt personnummer!");
            }
        }
        private int[] convertStringToIntArray(String personNummer) {
        int[] persNrAsIntArray = new int[10];
        int i = 0;
        int strLength = personNummer.length();
        if(strLength != 10) {
                System.out.println("Fel längd på personnummer");
        } 
        else {
            for(i=0;i<10;i++) {
                persNrAsIntArray[i] = Integer.parseInt(String.valueOf(personNummer.charAt(i)));
            }
        }
            return persNrAsIntArray;
        }
        public void kundCenter(Kund k){
            boolean grejerAttFixa = true;
            while(grejerAttFixa){
                System.out.println("1: Lägg till konto. 2: Ta bort konto. 3: Sätt in eller ta ut pengar.");
                System.out.println("4: Lista mina konton. 0: Avsluta");
                int userInputChoice = scanner.nextInt();
                switch(userInputChoice){
                    case 1:
                        this.nyttKontoKund(k);
                        break;
                    case 2:
                        this.tabortKontoKund(k);
                        break;
                    case 3:
                        this.hanteraSaldo(k);
                        break;
                    case 4:
                        this.listaKonton(k);
                        break;
                    case 0:
                        grejerAttFixa = false;
                        break;
                    default:
                        break;

                }
            }
        }
        private void nyttKontoKund(Kund verifieradKund){
            this.nästaKonto += 1;
            Konto konto = new Konto(verifieradKund,this.nästaKonto);
            if(verifieradKund.konton.add(konto)){
                System.out.println("Nya kontonumret är "+ this.nästaKonto);
            }
            else{
                System.out.println("Nytt konto gick inte att skapa");
            }
        }
        private void tabortKontoKund(Kund k){
            System.out.println("Vilket konto ska tas bort?");
            int dethär = scanner.nextInt();
            Iterator<Konto> i = k.konton.iterator();
            while (i.hasNext()) {
                Konto konto = i.next();
                if(konto.kontonummer == dethär){
                    i.remove();
                    System.out.println("Kontot är borttaget.");
                }
            }
        }
        private void listaKonton(Kund k){
            for(Konto kundenskonton: k.konton){
                System.out.println("Konto: " + kundenskonton.kontonummer
                                    + ". Saldo: " + kundenskonton.saldo);
            }
        }
        private void hanteraSaldo(Kund k){
            System.out.println("Ange kontonummer:");
            int dethär = scanner.nextInt();
            ArrayList<Konto> kundenskonton = k.konton;
            for(Konto konto : kundenskonton){
                if(konto.kontonummer == dethär){
                    System.out.println("Kontot är verifierat");
                    System.out.println("Ange negativt heltal för uttag, t.ex. -300");
                    System.out.println("Ange positivt heltal för insättning, t.ex. 200");
                    int transaktion = scanner.nextInt();
                    int saldo = konto.setSaldo(transaktion);
                    System.out.println("Nytt saldo är " + saldo);
                }
            }
        }
        public void kontroll(ArrayList allakunder){
            boolean inControl = true;
            while(inControl){
                System.out.println("1: Lägg till kund. 2: Ta bort kund. 3: Lägg till konto. 4: Ta bort konto.");
                System.out.println("5: Lista kunder och deras konton. 0: Avsluta.");
                int meny = scanner.nextInt();
                switch(meny){
                    case 1:
                        this.nyKund();
                        break;
                    case 2:
                        this.tabortKund();
                        break;
                    case 3:
                        this.nyttKonto();
                        break;
                    case 4:
                        this.tabortKonto();
                        break;
                    case 5:
                        for(Kund k: kunder){
                            System.out.println("Kundnummer " + Arrays.toString(k.personnummer) + ":");
                            for(Konto kundenskonton: k.konton){
                                System.out.println("Konto: " + kundenskonton.kontonummer
                                                    + ". Saldo: " + kundenskonton.saldo);
                            }
                        }
                        scanner.nextLine();
                        break;
                    case 0:
                        inControl = false;
                        break;
                    default:
                        break;
                }
            }
        }
        private void nyKund(){
            System.out.println("Ange personnummer för ny kund");
            String personNummer = scanner.next();
            int[] id = convertStringToIntArray(personNummer);
            if(Luhn(id)){
                Kund kund = new Kund(id);
                this.läggTillKund(kund);
            }else System.out.println("Felaktigt personnummer");
            
        }
        private void tabortKund(){
            System.out.println("Ange kundnummer");
            String personNummer = scanner.next();
            int[] id = convertStringToIntArray(personNummer);
            Iterator<Kund> i = kunder.iterator();
            while (i.hasNext()) {
                Kund kund = i.next(); // must be called before you can call i.remove()
                if(Arrays.equals(kund.personnummer, id)){
                    System.out.println("Kunden är identifierad, är du säker på att du vill ta bort den?");
                    System.out.println("1: Ja. 0: Nej.");
                    int val = scanner.nextInt();
                    switch(val){
                        case 1:
                            i.remove();
                            System.out.println("Kunden och dess konton är borttagna.");
                        case 0:
                            break;
                        default:
                            break;
                    }
                }
                else{
                    System.out.println("Den kunden gick inte att hitta.");
                }
            }
        }
        private void nyttKonto(){
            System.out.println("Ange kundnummer för nytt konto");
            String personNummer = scanner.next();
            int[] id = convertStringToIntArray(personNummer);
            boolean hittad = false;
            for(Kund kund : kunder){
                if(Arrays.equals(kund.personnummer, id)){
                    hittad = true;
                    System.out.println("Kunden är identifierad");
                    this.nästaKonto += 1;
                    Konto konto = new Konto(kund,this.nästaKonto);
                    if(kund.konton.add(konto)){
                        System.out.println("Nya kontonumret är "+ this.nästaKonto);
                    }
                    else{
                        System.out.println("Nytt konto gick inte att skapa");
                    }
                }
            }
            if(hittad==false){
                System.out.println("Den kunden gick inte att hitta.");
            }
        }
        private void tabortKonto(){
            System.out.println("Ange kundnummer");
            String personNummer = scanner.next();
            int[] id = convertStringToIntArray(personNummer);
            int denhär;
            for(Kund kund : kunder){
                if(Arrays.equals(kund.personnummer, id)){
                    System.out.println("Kunden är identifierad, kundens konton:");
                    for(Konto k: kund.konton){
                        System.out.println("Konto: " + k.kontonummer
                                            + ". Saldo: " + k.saldo);
                    }
                    System.out.println("Välj konto att ta bort:");
                    denhär = scanner.nextInt();
                    Iterator<Konto> i = kund.konton.iterator();
                    while (i.hasNext()) {
                        Konto konto = i.next();
                        if(konto.kontonummer == denhär){
                            i.remove();
                            System.out.println("Kontot är borttaget.");
                        }
                    }
                }
                else{
                    System.out.println("Den kunden gick inte att hitta.");
                }
            }
        }
        public void läggTillKund(Kund kund) {
            if (! kundRedanRegistrerad(kund)) {
                kunder.add(kund);
            }
        }

        private boolean Luhn(int[] luhnInput){
            if (luhnInput == null) return false; //returnera false om objektet Ã¤r null
            if (luhnInput.length != 10) return false; //kolla sÃ¥ att lÃ¤ngden Ã¤r 10
            
            int[] luhnCalc = new int[9]; // initiera en int-array att rÃ¤kna med i min for-loop
            int mod; // initiera en int att rÃ¤kna med i min for-loop, fÃ¶r att gÃ¥ngra med 1 eller 2


            //Denna for-loop tar ut de 9 fÃ¶rsta plats-vÃ¤rderna frÃ¥n luhnInput-arrayen
            //och multipliserar de Ã¶msom med 2 och 1 och sparar det i en ny array.
            //Blir produkten 10 eller mer sÃ¥ subtraherar man med 9
            for (int i = 0; i < 9; i++){

                    mod = (i + 1) % 2 + 1;

                    luhnCalc[i] = luhnInput[i] * mod; 

                    if (luhnCalc[i] > 9){

                            luhnCalc[i] -= 9;

                    }


            }

            //Denna for-loop rÃ¤knar ut summan av samtliga tal i luhnCalc-arrayen
            //och sparar den i en interger, calc

            int calc = 0;

            for (int i = 0; i < 9; i++){

                    calc += luhnCalc[i];


            }
            int calc2 = 0;
            //jag rÃ¤knar ut en kontrollsiffra genom att ta sista siffran calc, med hjÃ¤lp av modulus,
            //sparar den i calc2. Sedan subraherar jag 10 med calc2 och sparar resultatet
            //i en ny int som heter kontroll.
            //Ã„r kontroll det samma som sista vÃ¤rder i luhnInput-arrayen sÃ¥ stÃ¤mmer numret

            calc2 = calc % 10;
            int kontroll = 0;
            kontroll = 10 - calc2;
            if (luhnInput[9] == kontroll) return true;
            else return false;
        }
        private boolean Luhn(String luhnInput){
            
        	int[] persNrAsIntArray = convertStringToIntArray(luhnInput);
        	if (persNrAsIntArray == null) return false; //returnera false om objektet Ã¤r null
            if (persNrAsIntArray.length != 10) return false; //kolla sÃ¥ att lÃ¤ngden Ã¤r 10
            
            int[] luhnCalc = new int[9]; // initiera en int-array att rÃ¤kna med i min for-loop
            int mod; // initiera en int att rÃ¤kna med i min for-loop, fÃ¶r att gÃ¥ngra med 1 eller 2


            //Denna for-loop tar ut de 9 fÃ¶rsta plats-vÃ¤rderna frÃ¥n luhnInput-arrayen
            //och multipliserar de Ã¶msom med 2 och 1 och sparar det i en ny array.
            //Blir produkten 10 eller mer sÃ¥ subtraherar man med 9
            for (int i = 0; i < 9; i++){

                    mod = (i + 1) % 2 + 1;

                    luhnCalc[i] = persNrAsIntArray[i] * mod; 

                    if (luhnCalc[i] > 9){

                            luhnCalc[i] -= 9;

                    }


            }

            //Denna for-loop rÃ¤knar ut summan av samtliga tal i luhnCalc-arrayen
            //och sparar den i en interger, calc

            int calc = 0;

            for (int i = 0; i < 9; i++){

                    calc += luhnCalc[i];


            }
            int calc2 = 0;
            //jag rÃ¤knar ut en kontrollsiffra genom att ta sista siffran calc, med hjÃ¤lp av modulus,
            //sparar den i calc2. Sedan subraherar jag 10 med calc2 och sparar resultatet
            //i en ny int som heter kontroll.
            //Ã„r kontroll det samma som sista vÃ¤rder i luhnInput-arrayen sÃ¥ stÃ¤mmer numret

            calc2 = calc % 10;
            int kontroll = 0;
            kontroll = 10 - calc2;
            if (persNrAsIntArray[9] == kontroll) return true;
            else return false;
        }
    }
    public class Kund {
        private final int[] personnummer;
        public final ArrayList<Konto> konton = new ArrayList<>();
        
        public Kund(int[] personnummer) {
            this.personnummer = personnummer; //Varje kund har ett personnummer
        }
        public int[] getPnr() {
            return personnummer;
        }
        public void läggTillKonto(Konto konto) {
            konton.add(konto);
        }
    }
    public class Konto {
        private final int kontonummer;
        private int saldo; // Varje konto har ett saldo
        private final Kund kund;  // Varje konto har en ägarkund

        public Konto(Kund kund, int knr) {
            this.kund = kund;
            this.kontonummer = knr;
            this.saldo = 0;
        }
        public int getKontonr() {
            return kontonummer;
        }
        public int getSaldo() {
            return saldo;
        }
        public int setSaldo(int ändring) {
            return this.saldo += ändring;
        }
    }
}