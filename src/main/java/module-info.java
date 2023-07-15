module com.miguel.pi.cliente {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    
    requires de.jensd.fx.glyphs.fontawesome;
    requires MaterialFX;
    requires java.sql;
    requires org.json;

    opens com.miguel.pi.cliente to javafx.fxml;
    
    exports com.miguel.pi.cliente;
    exports com.miguel.pi.cliente.Utilidades;
    exports com.miguel.pi.cliente.Datos;
}
