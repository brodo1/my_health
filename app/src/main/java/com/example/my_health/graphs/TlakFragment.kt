package com.example.my_health.graphs

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.my_health.databinding.FragmentTlakBinding
import com.example.my_health.model.Tlak
import com.example.my_health.util.ButtonAddClickListener
import com.example.my_health.util.DateClickListener
import com.example.my_health.util.DeleteAll
import com.example.my_health.viewmodel.TlakViewModel
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.SimpleDateFormat
import java.util.*


class TlakFragment : Fragment(), DatePickerDialog.OnDateSetListener,DateClickListener,ButtonAddClickListener, DeleteAll {
    private lateinit var dataBinding: FragmentTlakBinding
    private lateinit var viewModel: TlakViewModel
    private var year=0
    private var month=0
    private var day=0
    private var minX=0.0
    private var maxX=0.0
    private var seriesLineGornji: LineGraphSeries<DataPoint> = LineGraphSeries()
    private var seriesLineDonji: LineGraphSeries<DataPoint> = LineGraphSeries()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding= DataBindingUtil.inflate<FragmentTlakBinding>(inflater, com.example.my_health.R.layout.fragment_tlak,container,false)
        dataBinding.listener=this //add
        dataBinding.listenerDate=this
        dataBinding.listenerDelete=this
        viewModel= ViewModelProvider(this).get(TlakViewModel::class.java)

        observe()


        deletePoint(seriesLineGornji)
        deletePoint(seriesLineDonji)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



    }


    fun observe(){
        viewModel.tlakovi.observe(viewLifecycleOwner, Observer {

            val (listagornji , listadonji)=getData(it)
            seriesLineGornji.resetData(listagornji.toTypedArray())
            //seriesPointGornji.resetData(listaGornji.toTypedArray())
            seriesLineDonji.resetData(listadonji.toTypedArray())
            makeGraph(0.0,0.0,0)

            if(it.isNotEmpty() ) {
                minX = listagornji[0].x
                maxX=listagornji[it.count()-1].x
                /*var dateMax= Date(minX.toLong())
                val calendar= Calendar.getInstance()
                calendar.time=dateMax
                calendar.add(Calendar.DATE,7)*/
                makeGraph(minX,maxX,it.count())
            }
        })

    }


    fun deletePoint( series:LineGraphSeries<DataPoint>){
        series.setOnDataPointTapListener { series, dataPoint ->

                val builder = AlertDialog.Builder(requireContext())

                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val xTocka = dataPoint.x.toLong()
                val datum = formatter.format(Date(xTocka))
                val yTocka = dataPoint.y

                builder.setTitle("Obriši vrijednost " + yTocka.toString() + ", " + datum + " ?")
                //builder.setMessage("obrisat ce se ")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

            builder.setPositiveButton("Yes") { dialog, which ->

                    viewModel.deleteTlak(xTocka)
                    Toast.makeText(requireContext(), "Obrisano!", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
            val alertDialog = builder.create()
            alertDialog.show()
            //Toast.makeText(requireContext(),dataPoint.y.toString(),Toast.LENGTH_SHORT).show()

            /*alertDialog.setOnKeyListener(object : DialogInterface.OnKeyListener {
                override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return true
                    }
                    if (keyCode == KeyEvent.KEYCODE_BACK){
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Obriši vrijednost " + yTocka.toString() + ", " + datum + " ?")
                        //builder.setMessage("obrisat ce se ")
                        builder.setIcon(android.R.drawable.ic_dialog_alert)

                        builder.setPositiveButton("Yes") { dialog, which ->

                            viewModel.deleteTlak(xTocka)
                            Toast.makeText(requireContext(), "Obrisano!", Toast.LENGTH_SHORT).show()
                            dialog.cancel()
                        }
                        builder.setNegativeButton("No") { dialog, which ->
                            dialog.cancel()
                        }
                    }
                    return false
                }

            })*/



        }
    }


    fun getData( lista:List<Tlak>):Pair<MutableList<DataPoint>,MutableList<DataPoint>>{
        var listaDPGornji: MutableList<DataPoint> = mutableListOf()
        var listaDPDonji: MutableList<DataPoint> = mutableListOf()
        if(lista.count()>0){
            for (i in lista.indices) {
                val gornjiy = lista[i].gornjiY
                val x = lista[i].X.toDouble()
                val donjiy = lista[i].donjiY

                val gornjiDataPoint = DataPoint(x, gornjiy)
                val donjiDataPoint = DataPoint(x, donjiy)

                listaDPGornji.add(gornjiDataPoint)
                listaDPDonji.add(donjiDataPoint)
            }
            listaDPGornji.sortBy { it.x }
            listaDPDonji.sortBy { it.x }
        }
        return Pair(listaDPGornji,listaDPDonji)
    }



    fun makeGraph(minX:Double,maxX:Double,len:Int){
        val graph=dataBinding.graph
        val viewport = graph.viewport
        val l:Int
        if(len>8){
            l=8
        }else{
            l=len
        }

        graph.addSeries(seriesLineGornji)
        seriesLineGornji.setDrawDataPoints(true)
        seriesLineGornji.setDataPointsRadius(25.toFloat())
        seriesLineGornji.setThickness(8)
        seriesLineGornji.setTitle("Gornji")
        //graph.addSeries(seriesPointGornji)
        graph.addSeries(seriesLineDonji)
        seriesLineDonji.setDrawDataPoints(true)
        seriesLineDonji.setDataPointsRadius(25.toFloat())
        seriesLineDonji.setThickness(8)
        seriesLineDonji.setTitle("Donji")
        seriesLineDonji.setColor(Color.RED)

        viewport.setScalable(true)
        viewport.setScalableY(true)

        //viewport.setMinY(70.0)
        viewport.setMinY(0.0)
        //viewport.setMaxY(150.0)
        viewport.setMaxY(200.0)
        viewport.setXAxisBoundsManual(true)

        viewport.setMinX(minX)
        viewport.setMaxX(maxX)
        viewport.setYAxisBoundsManual(true)


        val formatter = SimpleDateFormat("d/M/yy")
        graph.getGridLabelRenderer()
            .setLabelFormatter(DateAsXAxisLabelFormatter(requireContext(), formatter))


        graph.getGridLabelRenderer().setNumHorizontalLabels(l)
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(45);
        graph.getGridLabelRenderer().setNumVerticalLabels(9)
        graph.getGridLabelRenderer().getGridStyle().drawHorizontal()
        graph.getGridLabelRenderer().getGridStyle().drawVertical()
        graph.getGridLabelRenderer().setHumanRounding(true,true)
        graph.getGridLabelRenderer().setGridColor(Color.GRAY)


        //graph.getGridLabelRenderer().setLabelHorizontalHeight(100)
        //graph.getGridLabelRenderer().setLabelsSpace(30)



    }


    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

        Calendar.getInstance().let{
            it.set(year,month,day) //02-02-2022
            dataBinding.odaberiDatum.setText(day.toString().padStart(2,'0')+"-"
                    +(month+1).toString().padStart(2,'0')+"-"+year)
            this.year=year
            this.day=day
            this.month=month

        }

    }

    override fun onDateClick(v: View) {
        val calendar= Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker= DatePickerDialog(requireActivity(),this,year,month,day)

        val c=Calendar.getInstance() //moze se unijeti jedino vrijednost za datume vece od zadnjeg unesenog datuma
        c.time= Date(maxX.toLong())
        c.add(Calendar.DATE,1)
        datePicker.datePicker.minDate=c.time.time
        datePicker.show()
    }

    override fun onButtonAdd(v: View) {
           val tlak=Tlak(0,0.0,0.0)
            val calendar=Calendar.getInstance()
            calendar.set(year,month,day)
            val d1=calendar.time
        if(dataBinding.txtGornjiTlak.text.isNotEmpty() && dataBinding.txtDonjiTlak.text.isNotEmpty()
            && dataBinding.odaberiDatum.text.isNotEmpty() ) {

            tlak.gornjiY = dataBinding.txtGornjiTlak.text.toString().toDouble()
            tlak.donjiY = dataBinding.txtDonjiTlak.text.toString().toDouble()
            tlak.X = d1.time

            viewModel.insert(tlak)
        }
        else{
            Toast.makeText(requireContext(),"Unesi vrijednosti!",Toast.LENGTH_SHORT).show()
        }
        dataBinding.odaberiDatum.setText("")

    }



    override fun onDeleteAllClick(v: View) {
        viewModel.deleteAll()

    }


}