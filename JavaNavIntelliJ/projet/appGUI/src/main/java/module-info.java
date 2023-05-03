module sfa.nav.ui {
	requires org.slf4j;
	
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;
	requires javafx.swing;
	requires javafx.web;
	requires transitive javafx.graphics;
    requires JavaNav.appLib.main;

    opens sfa.nav;
	exports sfa.nav;
}