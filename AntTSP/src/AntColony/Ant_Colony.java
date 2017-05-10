/****************************************/
/*                                      */
/*		 EL�F UYAR - 13010011003		*/
/*		SENA �AKMAK - 13010011029       */
/*                                      */
/*                                      */
/****************************************/


package AntColony;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;


public class Ant_Colony implements Initializable {
	
	static Object[][] exelData;
	static int line = 0;
	static int sayac = 0; 

	@FXML
    private TextField id_alfa;
	@FXML
    private TextField id_beta;
	@FXML
    private TextField id_q ;
	@FXML
    private TextField id_feromon;
	@FXML
    private TextField id_buharlasma;
	@FXML
    private TextField id_karincasayisi;
	@FXML
    private TextField id_eniyiuzunluk;
	@FXML
    private TextField id_eniyitur;
	@FXML
	private TextField id_iterasyonSayisi;
	
	
	
	

	    // Algorithm parameters:
	    // Orijinal iz miktar�
	    private double c = 1.0;//Double.parseDouble(id_feromon.getText());//Double.parseDouble(id_feromon.getText());
	    // Iz tercihi
	    private double alfa = 1.0;
	    // A�g�zl� tercih
	    private double beta = 5;
	    // Iz buharla�ma katsay�s�
	    private double buharlasma = 0.5;
	    // Yeni iz dolgu katsay�s�
	    private double Q = 500;
	    // Kullan�lan kar�ncan�n say�s� = numAntFactor * numTowns
	    private double numAntFactor = 0.8;
	    // Bir sonraki kasaban�n saf rasgele se�ilme olas�l���
	    private double pr = 0.01;

	    // Makul say�da tekrarlama
	    // - results typically settle down by 500
	    private int maxIterasyon = 1000;
	   
	   

	    public int n = 0; // # Kasaba
	    public int m = 0; // # KAr�ncalar
	    private int graph[][] = null;
	    private double trails[][] = null;
	    private Ant ants[] = null;
	    private Random rand = new Random();
	    private double probs[] = null; //Form�lden gelen de�erler tutuluyor.

	    private int suankiIndex = 0;

	    public int[] eniyiTur;
	    public double eniyiTurUzunlugu;
	    static List<Double> eniyiSonuclar = new ArrayList<Double>();

	    // Kar�nca s�n�f�. Tur ve tabu bilgilerini korur.
	    
	   
	    @FXML 
		   private void baslat(ActionEvent event) throws IOException
		   {	
			   alfa = Double.parseDouble(id_alfa.getText());
			   beta = Double.parseDouble(id_beta.getText());
			   Q = Double.parseDouble(id_q.getText());
			   c = Double.parseDouble(id_feromon.getText());
			   maxIterasyon = Integer.parseInt(id_iterasyonSayisi.getText());
			   buharlasma = Double.parseDouble(id_buharlasma.getText());
			   numAntFactor = Double.parseDouble(id_karincasayisi.getText());
			   okuGraph();
			   cozumleme();
			   id_eniyiuzunluk.setText(Double.toString(eniyiTurUzunlugu - n));
			   id_eniyitur.setText(turToString(eniyiTur));
		   }
	    
	    private class Ant 
	    {
	        public int tur[] = new int[graph.length];
	        // Kasabalar i�in ziyaret edilen listeyi daha h�zl� tutun
	        // �u ana kadar turda olup olmad���n� kontrol etmekten.
	        public boolean gezilenler[] = new boolean[graph.length]; //Kar�ncalar�n gezdikleri �ehirleri tutar.

	        public void visitTown(int town)  /***********************************Z�YARET ED�LEN �LK �EH�R*************************************/
	        {
	            tur[suankiIndex + 1] = town;
	            gezilenler[town] = true;
				
	        }

	        public boolean gezilenler(int i) //Gezilen �ehirleri verir
	        {
	            return gezilenler[i];
	        }

	        public double turUzunlugu() // Tur uzunlu�unu hesaplar
	        {
	            double length = graph[tur[n - 1]][tur[0]];
	            for (int i = 0; i < n - 1; i++) 
	            {
	                length += graph[tur[i]][tur[i + 1]];
	            }
	            return length;
	        }

	        public void clear() //B�t�n �ehirleri gezilmeyen olarak i�aretlertr
	        {
	            for (int i = 0; i < n; i++)
	            {
	            	gezilenler[i] = false;
	            }                
	        }
	    }

	    // Bir dosyadan grafi�i okuyun.
	    // T�m belle�i tahsis eder.
	    // S�f�r uzunluk kenarlar�n�n olmamas�n� sa�lamak i�in kenar uzunluklar�na 1 ekler.
		
	    public void okuGraph() throws IOException /***********************************DOSYA OKUMA KISMI****************************************/
	    {
	        FileReader fr = new FileReader("ry48p.txt");
	        BufferedReader buf = new BufferedReader(fr);
	        String line;
	        int i = 0;

	        while ((line = buf.readLine()) != null) 
	        {
	            String splitA[] = line.split(" ");
	            LinkedList<String> split = new LinkedList<String>();
	            for (String s : splitA)
	            {
	            	if (!s.isEmpty())
	            	{
	            		split.add(s);
	            	}
	            }       

	            if (graph == null)
	            {
	                graph = new int[split.size()][split.size()];
	            }
	            int j = 0;

	            for (String s : split)
	            {
	            	if (!s.isEmpty())
	            	{
	            		graph[i][j++] = Integer.parseInt(s) +1 ;
	            	}
	            }
	            i++;
	        }
	        
	        //Dosyadakileri ekrana yazd�rd���m�z k�s�m
	        
	     /*  for(int i1 = 0; i1 <56; i1++)
	        {
	        	for(int j = 0; j< 56; j++)
	        	{
	        		System.out.print(graph[i1][j] + " " );
	        	}
	        	System.out.println();
	        }*/

	        n = graph.length;
	        m = (int) (n * numAntFactor);

	        // T�m bellek ay�rmalar�n�n yap�ld�g� k�s�m
	        trails = new double[n][n];
	        probs = new double[n];
	        ants = new Ant[m];
	        for (int j = 0; j < m; j++)
	        {
	        	ants[j] = new Ant();
	        }            
	    }

	    
	    public static double pow(final double a, final double b) 
	    {
	        final int x = (int) (Double.doubleToLongBits(a) >> 32);
	        final int y = (int) (b * (x - 1072632447) + 1072632447);
	        return Double.longBitsToDouble(((long) y) << 32);
	    }

	    // Her bir kasabaya ta��nma ihtimalini probs dizisinde saklar
	    // [1] bunlar�n nas�l hesapland���n� a��klar
	    // K�saca: Kar�ncalar, daha g��l� ve k�sa yol izlemeyi sever.
		
	    private void probTo(Ant ant)  /*********************************B�R TANE KARINCANIN �EH�R SE�ME OLASILI�I**********************************/
	    {
	        int i = ant.tur[suankiIndex];

	        double denom = 0.0;
	        for (int l = 0; l < n; l++)
	        {
	        	if (!ant.gezilenler(l))
	            {
	            	denom += pow(trails[i][l], alfa) * pow(1.0 / graph[i][l], beta); //Gezgin at�c�ya uyarlamak i�in 1/graph
	            } 
	        }
	            
	        for (int j = 0; j < n; j++) 
	        {
	            if (ant.gezilenler(j)) 
	            {
	                probs[j] = 0.0; //Gezilmi�se direkt 0 yap
	            } 
	            else 
	            {
	                double numerator = pow(trails[i][j], alfa) * pow(1.0 / graph[i][j], beta);
	                probs[j] = numerator / denom;
	            }
	        }
	    }

	    //Bir kar�nca, olas�l�klara dayal� bir sonraki kasabay� se�in
	    // her �ehre atayaca��z. Pr olas�l��� se�ildi�inde
	    // tamamen rasgele olarak (tabu listesi dikkate al�narak).
		
	    private int sonrakiSehriSec(Ant ant) /*******************************HER KARINCA ���N SONRAK� �EHR� BULUR*********************************/
	    {// Bazen rastgele se�
	        if (rand.nextDouble() < pr)
	        {
	            int t = rand.nextInt(n - suankiIndex); // rasgele kasaba
	            int j = -1;
	            for (int i = 0; i < n; i++) 
	            {
	                if (!ant.gezilenler(i))
	                {
	                	j++;
	                } 
	                if (j == t)
	                {
	                	return i;
	                }
	            }
	        }
	        // her kasaba i�in olas�l�klar� hesapla (probs'da saklan�r)
	        probTo(ant); //Form�l' den geliyor
	        // problara g�re rastgele se�
	        double r = rand.nextDouble();
	        double tot = 0;
	        for (int i = 0; i < n; i++) 
	        {
	            tot += probs[i];
	            if (tot >= r)
	            {
	            	return i; //Sonraki �ehrin index i 
	            }  
	        }
	        throw new RuntimeException("Not supposed to get here.");
	    }

	    // Kar�ncalar turlar�na dayal� yollar� g�ncelle
	    private void updateTrails()   //Buharla�ma oranlar�n� g�nceller
	    {// Buharla�ma
	        for (int i = 0; i < n; i++)
	        {
	        	for (int j = 0; j < n; j++)
	        	{
	        		trails[i][j] *= buharlasma;
	        	}  
	        }
	        // her kar�nca katk�s�
	        for (Ant a : ants) //Feromon bu form�le g�re hesaplan�r
	        {
	            double contribution = Q / a.turUzunlugu();
	            for (int i = 0; i < n - 1; i++) 
	            {
	                trails[a.tur[i]][a.tur[i + 1]] += contribution;
	            }
	            trails[a.tur[n - 1]][a.tur[0]] += contribution;
	        }
	    }

	    // Kar�ncalar i�in bir sonraki kasabay� se�
	    private void moveAnts()  /*************************************HER KARINCANIN G�DECE�� B�R SONRAK� EHR� SE�MES� ���N*************************************/
	    {// Her kar�nca parkurlar� izliyor ...
	        while (suankiIndex < n - 1)  //B�t�n �ehirleri gezer
	        {
	            for (Ant a : ants)  //B�t�n kar�ncalar tek tek
	            {
	            	a.visitTown(sonrakiSehriSec(a)); //Kar�nca i�in bir sonraki �ehri secer.
	            }
	            suankiIndex++;
	        }
	    }

	    // m kar�ncay� rasgele �ehirlerden ba�lat /************************************STEP1_KARINCALARI RANDOM �EH�RLERE YERLE�T�R�R************************/
	    private void karincalariYerlestir() 
	    {
	        suankiIndex = -1;
	        for (int i = 0; i < m; i++) 
	        {
	            ants[i].clear(); // Taze tahsisatlardan daha h�zl�.
	            ants[i].visitTown(rand.nextInt(n));
	        }
	        suankiIndex++;
	    }

	    private void updateEniyi()  /*****************************************EN �Y� TUR VE TUR UZUNLU�UNU BULAN FONKS�YON*******************************************/
	    {
	        if (eniyiTur == null) 
	        {
	            eniyiTur = ants[0].tur;
	            eniyiTurUzunlugu = ants[0].turUzunlugu();
	        }
	        for (Ant a : ants) 
	        {
	            if (a.turUzunlugu() < eniyiTurUzunlugu) 
	            {
	                eniyiTurUzunlugu = a.turUzunlugu();
	                eniyiTur = a.tur.clone();
	            }
	        }
	    }
	    

	    public static String turToString(int tur[])  /*******************************EN �Y� TURU STR�NG OLARAK YAZDIRMA*****************************************/
	    {
	    	
	        String t = new String();
	        
	        for (int i : tur)
	        {
	        	t = t + " " + i;	        	
	        } 	        
	        return t;
	    }

	    public int[] cozumleme() throws FileNotFoundException  /******************************��LEM� BA�LATTI�IMIZ KISIM*******************************/
	    {// izleri temizle
	        for (int i = 0; i < n; i++)
	        {
	        	for (int j = 0; j < n; j++)
	        	{
	        		trails[i][j] = c; 
	        	}
	        }
	        int iteration = 0;
	        // maxIterasyon i�in �al��t�r
	        // en iyi turu koruyun
	        
	        int satir = 0;
	        
	       
	        while (iteration < maxIterasyon) 
	        {
	            karincalariYerlestir();
	            moveAnts();
	            updateTrails();
	            updateEniyi();
	            System.out.println("En iyi tur uzunlugu: " + (eniyiTurUzunlugu - n));
	  	      
		        System.out.println("En iyi tur:" + turToString(eniyiTur));
		        
		        eniyiSonuclar.add(eniyiTurUzunlugu - n);

		        iteration++;
	        }
	        
	        
	        
	        //Y�k kald�r�rken kenara bir tane ekledi�imizden dolay� n ��kart�n
	        
	        return eniyiTur.clone();
	    }
	    
	    
	    
	    static HSSFWorkbook workbook;
	    public static void createExcelFile(List<Double> list) throws IOException
	    {
	      //Excel Calisma Kitabini Olustur
	      workbook = new HSSFWorkbook();
	      //Excel Sayfasi Olustur
	      HSSFSheet sheet = workbook.createSheet("En �yi De�erler");
	       
	      //Basliklar icin olusturulacak bicim yapisi icin font nesnesi hazirla
	      HSSFFont headerFont = workbook.createFont();
	       
	      //Yazi stili kalin
	      headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	       
	      //Yazi rengini belirle
	      headerFont.setColor(IndexedColors.BLUE.getIndex()); 
	       
	      //Basliklar icin bicim nesnesini olustur
	      HSSFCellStyle headerStyle = workbook.createCellStyle();
	       
	      //Hazirladigin Font nesnesini bicime ekle
	      headerStyle.setFont(headerFont);
	       
	      //Basliklari Hazirla
	      
	      
	    /*	  Row headerRow = sheet.createRow(0);
		      Cell hname = headerRow.createCell(sayac);
		      hname.setCellValue(sayac);
		      
		      
		    //Olusturulan baslik bicimini hucrelere ekle
		      hname.setCellStyle(headerStyle);*/
		      
		     // cozumleme();
	

	      //Listeyi Yaz
	      Row row0 ;
	      Cell name0 ;
	      int sutun =0;
	      for(int i = 0; i < list.size(); i++)
	      {
	        //Olusturdugumuz sayfa icerisinde yeni bir satir olustur
	        //i+1 nedeni 0. sat�r yani ilk satira basliklari yazdigimiz icin 0 dan baslatmadik
	       
	         
	    	 
	    		/*  row = sheet.createRow(i+1);
		   	      //Ilgili satir icin yeni bir hucre olustur
		   	        name = row.createCell(0);
		   	        name.setCellValue(list.get(i));*/
	    	  
	    	  if(i>=0 && i<list.size())
	    	  {
	    		  if(i == 0)
		        	{
		        		line = 0;
		        	}
	    		  row0 = sheet.createRow(line);
		        	name0 = row0.createCell(0);
			        name0.setCellValue(list.get(i));
			        line ++;
	    		  
	    		  
	    	  }
	    	 
	      
	      }
	      //Olusturulan Excel Nesnesini Dosyaya Yaz
	      workbook.write(out);
	    }
	    
	    
	    
	    static FileOutputStream out;
	    public static void olustur() throws FileNotFoundException
	    {
	    	 out = new FileOutputStream(new File("C:\\Users\\User\\workspacee\\AntTSP\\veriler.xls"));
	    }
	    
	    public static void write(HSSFWorkbook workbook)
	    {
	     try
	     {	
	    		 workbook.write(out); 
	    		// out.close();
    
	     } 
	     catch (FileNotFoundException e) {
	       e.printStackTrace();
	     } 
	     catch (IOException e) {
	          e.printStackTrace();
	     }
	    }
	     
	    
	   
	   
	 
	    static int sutun = 0;
	    public static void main(String[] args) throws IOException 
	    {
	    	olustur();
	    	
	    	
	    		Ant_Colony anttsp = new Ant_Colony();
		    	anttsp.okuGraph(); //Dosya okuma k�sm�
		    	anttsp.cozumleme();  //en k�sa yolu ve uzunlugunu buldu�umuz k�s�m
		 
	    	createExcelFile(eniyiSonuclar);
	    	workbook.write(out);
	    	out.close();
	    	
	    }

	

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			  
		}
		
		
		
}
