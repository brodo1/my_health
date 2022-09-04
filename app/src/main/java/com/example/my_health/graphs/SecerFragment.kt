package com.example.my_health.graphs

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.my_health.R
import com.example.my_health.databinding.FragmentSecerBinding
import com.example.my_health.databinding.FragmentTlakBinding
import com.example.my_health.model.Secer
import com.example.my_health.model.Tlak
import com.example.my_health.util.ButtonAddClickListener
import com.example.my_health.util.DateClickListener
import com.example.my_health.util.DeleteAll
import com.example.my_health.viewmodel.SecerViewModel
import com.example.my_health.viewmodel.TlakViewModel
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.OnDataPointTapListener
import com.jjoe64.graphview.series.Series
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class SecerFragment : Fragment(), DatePickerDialog.OnDateSetListener, DateClickListener,
    ButtonAddClickListener, DeleteAll{
    private lateinit var dataBinding: FragmentSecerBinding
    private lateinit var viewModel: SecerViewModel
    private var year=0
    private var month=0
    private var day=0
    private var minX=0.0
    private var maxX=0.0
    private var seriesLine: LineGraphSeries<DataPoint> = LineGraphSeries()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding= DataBindingUtil.inflate<FragmentSecerBinding>(inflater, com.example.my_health.R.layout.fragment_secer,container,false)
        dataBinding.listener=this //add
        dataBinding.listenerDate=this
        dataBinding.listenerDelete=this
        viewModel= ViewModelProvider(this).get(SecerViewModel::class.java)

        observe()

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            seriesLine.setOnDataPointTapListener { series, dataPoint ->

                val builder = AlertDialog.Builder(requireContext())

                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val xTocka = dataPoint.x.toLong()
                val datum = formatter.format(Date(xTocka))
                val yTocka = dataPoint.y

                builder.setTitle("Obriši vrijednost " + yTocka.toString() + ", " + datum + " ?")
                //builder.setMessage("obrisat ce se ")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                builder.setPositiveButton("Yes") { dialog, which ->

                    viewModel.deletesecer(xTocka)
                    Toast.makeText(requireContext(), "Obrisano!", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }

        super.onViewCreated(view, savedInstanceState)
    }

    fun observe(){
        viewModel.seceri.observe(viewLifecycleOwner, Observer {

            val lista =getData(it)
            seriesLine.resetData(lista.toTypedArray())

            makeGraph(0.0,0.0,0)

            if(it.isNotEmpty() ) {
                minX = lista[0].x
                maxX=lista[it.count()-1].x
                val dateMax= Date(minX.toLong())
                val calendar= Calendar.getInstance()
                calendar.time=dateMax
                calendar.add(Calendar.DATE,7)
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

                viewModel.deletesecer(xTocka)
                Toast.makeText(requireContext(), "Obrisano!", Toast.LENGTH_SHORT).show()
                dialog.cancel()
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            val alertDialog = builder.create()
            alertDialog.show()

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


    fun getData( lista:List<Secer>):MutableList<DataPoint>{
        var listaDP: MutableList<DataPoint> = mutableListOf()

        if(lista.count()>0){
            for (i in lista.indices) {
                val y = lista[i].Y
                val x = lista[i].X.toDouble()
                val dataPoint = DataPoint(x, y)
                listaDP.add(dataPoint)
            }
            listaDP.sortBy { it.x }

        }
        return listaDP
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

        graph.addSeries(seriesLine)
        seriesLine.setDrawDataPoints(true)
        seriesLine.setDataPointsRadius(25.toFloat())
        seriesLine.setThickness(8)



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
            dataBinding.txtDatumSecer.setText(day.toString().padStart(2,'0')+"-"
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
        val secer= Secer(0,0.0)
        val calendar=Calendar.getInstance()
        calendar.set(year,month,day)
        val d1=calendar.time
        if(dataBinding.txtSecer.text.isNotEmpty() && dataBinding.txtDatumSecer.text.isNotEmpty() ) {

            secer.Y = dataBinding.txtSecer.text.toString().toDouble()
            secer.X = d1.time

            viewModel.insert(secer)
        }
        else{
            Toast.makeText(requireContext(),"Unesi vrijednosti!", Toast.LENGTH_SHORT).show()
        }
        dataBinding.txtDatumSecer.setText("")

    }



    override fun onDeleteAllClick(v: View) {
        viewModel.deleteAll()

    }




}