/**
	MIS Project, Common Variables
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package commons;

public class CommonVariables {
	
	// Se si varia NumberOfClient N, si deve variare anche intervalAvg, stabObs  
	// e throughputObs(facoltativo se N maggiore del precedente) !!!
	public static int numberOfClient = 12; 
		
	// FISSO a 3, per come trattati i getStabState/setStabState dell'Observer
	public static int numberOfServer = 3; 
	
	public static int FIFOQueue = 0;
	public static int LIFOQueue = 1;
	public static int RANDQueue = 2;
	
	public static int seedRandom = 13666321;
	public static int seedRandQueue = 19891651;
	public static int seedExit = 27887532;
	public static int seedRouting = 3913;
	public static int seedRandObs = 5331;
	
	public static int seedTerm = 1031;
	public static int seedCPUClient = 8779;
	public static int seedLAN1 = 5537;
	public static int seedWAN = 7713;
	public static int seedLAN2 = 9999;
	public static int seedGW1 = 7777;
	public static int seedGW2 = 3333;
	public static int seedSPC = 6117;
	public static int seedMs = 81973;
	
	public static double exitProbability = 0.0196;

	public static int numberOfRun = 50;
	
	public static int stabilizationObs = 1;
	
	// Valori utilizzati per trovare in automatico (dopo stopStab volte)
	// il numero di osservazioni (StabObs) in cui consideriamo il sistema stabile! 
	// Cambiati poi, a seconda dei CLIENT, in Stabilizator!!!
	public static int stopStab = 100; 	
	public static double intervalAvg = 0.1; 
	public static double intervalVar = 0.1;
	
	// Se l'utente avvia stabilizzazione userStab = true -> a fine stabilizzazione
	// verrà richiesto, dopo aver considerato i grafici, di inserire il numero di
	// osservazioni per cui ci si considera stabili (in StabObsUser)
	public static boolean userStab = false;
	public static int stabObsUser = 1;
	
	// N=JOB->OsservazioniDiPartenzaDopoStatoStabile
	public static int stabObs12 =  300;
	public static int stabObs24 =  400; 	
	public static int stabObs30 =  500; 	
	public static int stabObs40 =  600; 	
	public static int stabObs60 =  1000; 	
	public static int stabObs80 =  1500; 	
	public static int stabObs100 =  2000; 	
	
	// Numero di Job N, per il quale si osserva il Throughput del centro WAN
	public static int throughputObs = 12;
	
	// Tempo di Stabilizzazione trascorso, dopo il numero di osservazioni trovate precedentemente,
	// quando si chiama this.sched.simulateRun(obs) (inizialmente a 0, poi impostato automaticamente)
	public static double timeStab = 0.0; 
		
	public static double u_alfa = 1.645;
	
	// RoundTripTime, utilizzato per Osservazione del Throughput, calcolato come:
	//	0,00016 sec	E(ts_LAN1,c1) 	+	
	//	0,0005 sec	E(ts_GW1,c1)	+
	//	0,003 sec	E(ts_WAN,c1)	+
	//	0,0005 sec	E(ts_GW2,c1)	+
	//	0,00032 sec	E(ts_LAN2,c1)	+
	//	0,144024 sec	(0,002824 E(ts_SPc) * 51 nMs+1)		+  
	//	1,66665	sec		(0,033333 E(ts_Ms) * 50 nMs)		+ 
	//	0,00016	sec	E(ts_LAN2,c2)	+
	//	0,0005 sec	E(ts_GW2,c2)	+
	//	0,003 sec	E(ts_WAN,c2)	+
	//	0,0005 sec	E(ts_GW1,c2)	+
	//	0,00008	sec	E(ts_LAN1,c2)	=    1.819394 sec (da LAN1 a LAN1)
	
	public static double RTT = 1.819394;

	// Fattore Moltiplicativo che, per OsservazioneThroughput (in Scheduler at WAN Centre)
	// e insieme a RTT, fornisce la dimensione della finestra d'osservazione
	public static int multiplicativeFactor = 6;
	
	// Variabili per stampe da console e grafici
	public static boolean printSim = true;
	public static boolean printSched = true;
	public static boolean printSchedCalendar = false;
	public static boolean printSchedJobs = false;
	public static boolean printSchedQueue = false;
	
	public static boolean printStabRun = true;
	public static boolean printStabGordon = true;
	public static boolean printObs = true;
	public static boolean printObsReturns = false;
	public static boolean printThroughput = false;
	
	public static boolean graphThroughput = true;
	public static boolean graphStabState = true;
}