package br.com.pitdog.controller.sys;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.pitdog.model.global.Cliente;
import br.com.pitdog.model.mov.Venda;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.global.ClienteService;
import br.com.pitdog.service.mov.VendaService;
import br.com.pitdog.util.DateUtil;

@ViewScoped
@Named
public class GraficoController implements Serializable {

	private static final long serialVersionUID = -1049288202883406628L;

	private BarChartModel barChartModel;
	
	@Inject
	private VendaService vendaService;
	
	@Inject
	private ClienteService clienteService;
	
	@PostConstruct
	public void init() {
		createAnimatedModels();
	}
	
	public Long quantidadeComprasPorCliente(Cliente cliente){
		return vendaService.quantidadeComprasPorCliente(cliente);
	}
	
	public Long quantidadeTotalComprasCliente(){
		return vendaService.quantidadeTotalComprasCliente();
	}

	private void createAnimatedModels() {
		barChartModel = initBarModel();
		barChartModel.setTitle("Gráfico mensal de quantidade compras realizados por clientes");
		barChartModel.setAnimate(true);
		barChartModel.setLegendPosition("ne");
		Axis yAxis = barChartModel.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(quantidadeTotalComprasCliente());
	}

	private BarChartModel initBarModel() {
		BarChartModel model = new BarChartModel();

		ChartSeries clientesCharts = new ChartSeries();
		clientesCharts.setLabel("Cliente");
		model.addSeries(gerarCalendarioMensal(clientesCharts));

		return model;
	}
	
	private ChartSeries gerarCalendarioMensal(ChartSeries clientesCharts){
		List<String> lista = new ArrayList<String>();
			for(Cliente cliente : clienteService.findAll()){
				List<Venda> vendasPorCliente = vendaService.buscarVendasPorCliente(cliente);
				for(Venda venda : vendasPorCliente){
					clientesCharts.set(adicionarMesesAno(DateUtil.asLocalDate(venda.getData())),vendasPorCliente.size());
					lista.add(adicionarMesesAno(DateUtil.asLocalDate(venda.getData())) + " " +vendasPorCliente.size());
				}
			}
			System.out.println(lista);
		return clientesCharts;
	}
	
	private static String adicionarMesesAno(LocalDate localDate){
		DateFormat df = new SimpleDateFormat("MMMM"); 
		String data = df.format(DateUtil.asDate(localDate));
		return data;
	}

	public BarChartModel getBarChartModel() {
		return barChartModel;
	}

	public void setBarChartModel(BarChartModel barChartModel) {
		this.barChartModel = barChartModel;
	}

}
