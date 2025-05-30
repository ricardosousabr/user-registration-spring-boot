package com.ghost.first_spring_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstSpringAppApplication {


	public static void main(String[] args) {
		SpringApplication.run(FirstSpringAppApplication.class, args);

		class Mother {
			String eyeColor;
			float height;
			String skinTone;
			String gender;
			String hairColor;

			void Sing() {
				System.out.println("Gosta de cantar");
			}
		}

		class Son extends Mother {

			void Sport() {
				System.out.println("Gosta de esportes");

			}

			Son(String eyeColor, float height, String skinTone,  String gender, String hairColor){
				this.eyeColor = eyeColor;
				this.height = height;
				this.skinTone = skinTone;
				this.gender = gender;
				this.hairColor = hairColor;

			};

			Son(String eyeColor, String skinTone, String hairColor){
				this.eyeColor = eyeColor;
				this.skinTone = skinTone;
				this.hairColor = hairColor;

			};
		}

		Son son1 = new Son("Green", 1.60f, "Brown", "Masculine", "Black");

		Son son2 = new Son("Green", "Brown", "Black");

		System.out.println("Eye color: " + son1.eyeColor + " height: " + son1.height + " Skin tone: " + son1.skinTone + " Gender: " + son1.gender + " Hair color: " + son1.hairColor);
		System.out.println("Eye color: " + son2.eyeColor + " Skin tone: " + son2.skinTone + " Hair color: " + son2.hairColor);
	}
}
