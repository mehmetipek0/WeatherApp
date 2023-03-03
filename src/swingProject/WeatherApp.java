package swingProject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JSeparator;

public class WeatherApp extends JFrame {

	private JPanel contentPane;
	private static Document doc;
	private static Elements body;
	private static Logger logger = LogManager.getLogger(WeatherApp.class.getName());
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WeatherApp frame = new WeatherApp();
					frame.setVisible(true);
					BasicConfigurator.configure();
					logger.info("Opening successful.");
				} catch (Exception e) {
					logger.error("Can not connect the URL.");
					e.printStackTrace();
				}
			}
		});
	}

	public WeatherApp() throws IOException {
		
		setTitle("WeatherApp");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(91, 143, 185));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		doc = Jsoup.connect("https://www.timeanddate.com/weather/?sort=1").timeout(6000).get();
		body = doc.select("tbody");
		
		//Getting the city info's into the cities[],temperatures[],time[], forecast[] string arrays.
		logger.info("Getting the city infos...");
		String[] cities = new String[body.select("tr td a").size()];
		String[] temperatures = new String[body.select("tr td.rbi").size()];
		String[] time = new String[141];
		String[] forecast = new String[141];
		int i = 0;int j = 0;int k=1; int l=0; int m=0; int n=0;int x = 0;
		for(Element e: body.select("tr td")) {
			if(i%4==0) {
				cities[j] = e.select("a").text();
				j++;		
			}
			i++;
			if(k%4==0) {
				temperatures[l] = e.select("[class=rbi]").text();
				l++;
			}
			k++;
			if(m%4==1) {	
				time[n] = e.select("td.r").text();
				n++;
			}
			m++;
			for(Element e1: body.select("img")) {
				forecast[x] = e1.attr("title");
				if(x<140) {
					x++;
				}
				else {
					x=0;
				}
			}
		}
		//End of the getting values.	
		logger.info("Getting infos succesful!");
		//Creating the components of the JFrame.
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(41, 41, 306, 33);
		contentPane.add(comboBox);
		
		JLabel lblNewLabel = new JLabel("Please select a city.");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(41, 10, 130, 33);
		comboBox.setModel(new DefaultComboBoxModel(cities));
		contentPane.add(lblNewLabel);
		
		JLabel cityName = new JLabel("");
		cityName.setBackground(Color.GRAY);
		cityName.setForeground(new Color(3, 0, 28));
		cityName.setFont(new Font("Tahoma", Font.BOLD, 14));
		cityName.setBounds(41, 149, 306, 33);
		contentPane.add(cityName);
		
		JLabel cityTemperature = new JLabel("");
		cityTemperature.setForeground(new Color(3, 0, 28));
		cityTemperature.setFont(new Font("Tahoma", Font.PLAIN, 24));
		cityTemperature.setBounds(60, 192, 111, 58);
		contentPane.add(cityTemperature);
		
		JLabel cityTime = new JLabel("");
		cityTime.setForeground(new Color(3, 0, 28));
		cityTime.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cityTime.setBounds(236, 190, 111, 60);
		contentPane.add(cityTime);
		
		JLabel cityForecast = new JLabel("");
		cityForecast.setForeground(new Color(3, 0, 28));
		cityForecast.setFont(new Font("Tahoma", Font.PLAIN, 16));
		cityForecast.setBounds(70, 260, 250, 33);
		contentPane.add(cityForecast);
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		refreshButton.setBounds(253, 303, 94, 33);
		contentPane.add(refreshButton);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(new Color(192, 192, 192));
		separator.setBounds(30, 35, 330, 10);
		contentPane.add(separator);
		
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Selected a city");
				//Selecting a city in the comboBox.
				try {
					doc = Jsoup.connect("https://www.timeanddate.com/weather/?sort=1").timeout(6000).get();
				} catch (IOException e1) {
					logger.error("Can not connect the URL.");
					e1.printStackTrace();
				}
				body = doc.select("tbody");
				//Getting the values in the HTML again.
				logger.info("Getting the city infos...");
				int i = 0;int j = 0;int k=1; int l=0; int m=0; int n=0; int y=0;
				for(Element e1: body.select("tr td")) {
					if(i%4==0) {
						cities[j] = e1.select("a").text();
						j++;		
					}
					i++;
					if(k%4==0) {
						temperatures[l] = e1.select("[class=rbi]").text();
						l++;
					}
					k++;
					if(m%4==1) {	
						time[n] = e1.select("td.r").text();
						n++;
					}
					m++;
				}				
				for(Element e2: body.select("img")) {
					forecast[y] = e2.attr("title");
					y++;
					y = y%140;
				}
				logger.info("Getting is succesful!");
				String x = comboBox.getSelectedItem().toString();
				for(i=0;i<141;i++) {
					if(x.equals(cities[i])) {
						break;
					}
				}
				cityName.setText(cities[i]);
				cityTemperature.setText(temperatures[i]);
				cityTime.setText(time[i]);
				cityForecast.setText(forecast[i]);
				
			}
		});
		
		//If refresh button is clicked.
		refreshButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				logger.info("Refresh button clicked...");
				try {
					doc = Jsoup.connect("https://www.timeanddate.com/weather/?sort=1").timeout(6000).get();
				} catch (IOException e1) {
					logger.error("Can not connect the URL.");
					e1.printStackTrace();
				}
				body = doc.select("tbody");
				logger.info("Getting the informations in the website...");
				//Getting the values in the HTML again.
				int i = 0;int j = 0;int k=1; int l=0; int m=0; int n=0; int y=0;
				for(Element e1: body.select("tr td")) {
					if(i%4==0) {
						cities[j] = e1.select("a").text();
						j++;		
					}
					i++;
					if(k%4==0) {
						temperatures[l] = e1.select("[class=rbi]").text();
						l++;
					}
					k++;
					if(m%4==1) {	
						time[n] = e1.select("td.r").text();
						n++;
					}
					m++;
				}				
				for(Element e2: body.select("img")) {
					forecast[y] = e2.attr("title");
					y++;
					y = y%140;
				}
				String x = comboBox.getSelectedItem().toString();
				for(i=0;i<141;i++) {
					if(x.equals(cities[i])) {
						break;
					}
				}
				logger.info("Getting information succesful!");
				cityName.setText(cities[i]);
				cityTemperature.setText(temperatures[i]);
				cityTime.setText(time[i]);
				cityForecast.setText(forecast[i]);
			}
		});
	}
}

