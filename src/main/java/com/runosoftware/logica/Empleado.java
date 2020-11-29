/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.runosoftware.logica;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Vidal
 * 
 * se crea la identidad de entity para adniministrar como tabla de la bse de datos
 */

@Entity
@Table(name = "Empleado")
public class Empleado implements Serializable{
    
    private static final long serialVersionUID =1L;
    
    //atributos y columnas sde la tabla 
    
    @Id
    @Column(name = "codigo")
    private int codigo;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "apellido")
    private String apellido;
    
    @Column(name = "fechaNacimiento")
    private Date fechaNacimiento;
    
    //contructores

    public Empleado() {
    }

    public Empleado(int codigo, String nombre, String apellido, Date fechaNacimiento) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return "Empleado{" + "codigo=" + codigo + ", nombre=" + nombre + 
                ", apellido=" + apellido + ", fechaNacimiento=" + fechaNacimiento + '}';
    }
    
    
    // setter and getter 

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
   
}
