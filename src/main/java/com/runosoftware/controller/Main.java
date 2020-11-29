/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runosoftware.controller;

/**
 *principal para desplegar la app , al ser con maven solo se descarga las dependecias en el
 * archivo pom.xml y se crea todo con la base de datos embebida.
 * NOTA: solo cambiar despues de la primera ejecucion en el archivo persistence.xml 
 *de value="create-drop" a value="validate"
 * para que la base no se elimine y se guarde los registros
 * 
 * 
 * @author Vidal
 */


public class Main {
    public static void main(String[] args) {
        controllerEmpleado control = new controllerEmpleado();
        control.mostrarVentana();
    }
    
}
