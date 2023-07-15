package app.client.utilidades.utilObjects;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;

public class GlassPopup extends PopupPanel{


	/**
	 * PopupPanel que cubre la pantalla dando un efecto vidriado al fondo. 
	 * Para su utilización se debe crear un Popup propio que extienda esta clase. 
	 * 
	 * Se necesita además, configurar el estilo de la aplicación y el html de la siguiente forma 
	 * 
	 * HTML: 
	 * 
	<body>
		 <div id="screen">
				<div id="application"></div>
		</div>
	</body>
	 * 
	 * CSS:
	 * 
	// DIV que contiene el DIV de la aplicación
	#screen {
	    height: 100%;
	    width: 100%;
	    overflow: scroll;
	    position: relative;
	}

	// Estilo del DIV que contiene la aplicación, se debe hacer RootPanel.get("application");
	#application {
	    width: 80%;
	    margin: 0 auto;
	}

	// Estilo del panel de fondo, por defecto gris transparente
	.glassPopupPanel {
		width: 100%;
		height: 100%;
		background-color: #000;
		opacity: 0.70;
		-moz-opacity: 0.70;
		filter: alpha(opacity=70);
	}


	body,html {
		margin: 0px;
		height: 100%;
	    width: 100%;
	    overflow: hidden;
	}
	 * 
	 * @author nico
	 * 
	 */

		/**
		 * Panel que coloca una capa gris sobre el fondo
		 */
		private PopupPanel glassPanel = new PopupPanel();
		
		public GlassPopup() {

			/*********************************************
			 * A glass panel or 'blinder' to wash out the current screen
			 ********************************************/
			glassPanel.setStyleName("glassPopupPanel");
			glassPanel.getElement().getStyle().setProperty("height", Window.getClientHeight()*1.5	 + "px");

		}

		@Override
		public void show() {
			glassPanel.show();
			super.show();
			super.center();
		}
		
		@Override
		public void hide() {
			glassPanel.hide();
			super.hide();
		}
		
		
}
