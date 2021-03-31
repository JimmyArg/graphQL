package definitions;



import io.cucumber.java.en.Given;
import net.thucydides.core.annotations.Steps;
import step.ServicioStep;


import java.io.IOException;


public class definitions {

    @Steps
    ServicioStep step;

    @Given("consultar servicio")
    public void consultar_servicio() throws IOException {
        step.conexionDelServico();
        step.conexionDelServico2("id");
        step.conexionDelServicosinevidenci();
        step.conexionDelServico2Sinevidencia();
    }


}
