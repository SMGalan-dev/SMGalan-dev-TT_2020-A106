package com.example.tt_a106_v0.TESTActivitys

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_a106_v0.R
import com.example.tt_a106_v0.Users_register.MainRegisterActivity
import com.example.tt_a106_v0.patient_fragments.ForgotPasswordActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class PDFtest_withAuthActivity : AppCompatActivity() {
    //SplashScreen
  //  private lateinit var binding: ActivityAuthBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(3000)
        setTheme(R.style.Theme_TT_A106_v0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        //  Setup
        setup()
    }

    private fun setup(){
    //    binding = ActivityAuthBinding.inflate(layoutInflater)
      //  setContentView(binding.root)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val logInButton = findViewById<Button>(R.id.logInButton)
        val radioGrupo = findViewById<RadioGroup>(R.id.radioGroup1)
        val Paciente = findViewById<RadioButton>(R.id.usePaciente)
        val Familiar = findViewById<RadioButton>(R.id.useFamiliar)
        val Doctor = findViewById<RadioButton>(R.id.useDoctor)
        val ForgotPssw = findViewById<TextView>(R.id.twForgotPassword)

        title = "Autenticación"
        var seleccionar: Int

        ForgotPssw.setOnClickListener {
            val psswIntent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(psswIntent)

        }

        signUpButton.setOnClickListener {
            val registerUserIntent = Intent(this, MainRegisterActivity::class.java)
            startActivity(registerUserIntent)
        }

            logInButton.setOnClickListener {

            createPDF(emailEditText)
                //createFile()

                /*

            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                       if(Paciente.isChecked) {
                            val intoUserIntent = Intent(this, MainActivityPatient1::class.java)
                            startActivity(intoUserIntent)
                       }else if(Familiar.isChecked) {
                            val intoUserIntent = Intent(this, MainActivityFamiliar::class.java)
                            startActivity(intoUserIntent)
                        }else if (Doctor.isChecked){
                            val intoUserIntent = Intent(this, MainActivityDoctor::class.java)
                            startActivity(intoUserIntent)
                        }
                    } else{
                        showAlert()
                    }
                }
            }
                 */
        }
    }
    private fun createPDF(emailEditText: EditText) {
        Toast.makeText(this, "Generando reporte...", Toast.LENGTH_LONG).show()

        //val fileLocation = File( this.getExternalFilesDir("/")).toString()

        val reportFile = File( this.getExternalFilesDir("/"), "Reporte_${emailEditText.text.toString()}.pdf")
        val outputStream = FileOutputStream(reportFile)
        //val fileLocation = File( this.getExternalFilesDir("/"), "nombrePDF.pdf")
        //val pdfDocument = PdfDocument(PdfWriter(reportFile))
        val writer = PdfWriter(reportFile)
        val pdfDocument = PdfDocument(writer)   // supply this instance to com.itextpdf.kernel.pdf.PdfDocument

        val document = Document(pdfDocument)    // supply this instance to create com.itextpdf.layout.Document

        val iconGctrl = getDrawable(R.drawable.ic_glucontrol)
        val bitmap = (iconGctrl as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()

        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        image.setHeight(100f)
        image.setWidth(100f)
        document.add(image)

        // This will create a table instance with weighted column width
        val table = Table(UnitValue.createPercentArray(floatArrayOf(8f, 23f, 15f, 15f, 12f, 12f, 15f))).useAllAvailableWidth()

        //Add Header Cells
        table.addHeaderCell(Cell().add(Paragraph("Date").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Job Name").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Job Size").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Job Type").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Quantity").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Rate").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)))

        /*
        for (entry in entries) {
            table.addCell(Cell().add(Paragraph(shortDateFormat.format(entry.createdOn)).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(entry.getJobName())
            table.addCell(Cell().add(Paragraph(entry.jobSize).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(entry.getJobType().replace("Pouch", "")).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(entry.quantity).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(entry.rate).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(entry.amount).setTextAlignment(TextAlignment.RIGHT)))
        }
         */

        document.add(table)

        document.close()
    }

   /*
    private fun createPDFIncorporedCanvas(){
        Toast.makeText(this, "Generando reporte...", Toast.LENGTH_LONG).show()
        val newPDF = PdfDocument()
        val paintG = Paint()

        val pageFormat = PdfDocument.PageInfo.Builder(250, 400, 1).create()
        val firstPage = newPDF.startPage(pageFormat)

        val canvas = firstPage.canvas

        canvas.drawText("Paginita PDF de prueba", 40F, 50F, paintG)
        newPDF.finishPage(firstPage)
        val file = File( this.getExternalFilesDir("/"), "nombrePDF.pdf")
        //val file = File( "${Environment.getDataDirectory()}", "nombrePDF.pdf")
        Toast.makeText(this, "${this.getExternalFilesDir("/")}", Toast.LENGTH_LONG).show()
        Log.e("jk","${this.getExternalFilesDir("/")}" )

        if (!file.exists()) { // Si no existe, crea el archivo.
            try {
                file.createNewFile()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }

        try {
            newPDF.writeTo(FileOutputStream(file))
        }catch (e: IOException){
            e.printStackTrace()
        }

        newPDF.close()
    }

    */


    /*

    private fun crateWithITextFirstFunctionableLayout(emailEditText: EditText) {
        Toast.makeText(this, "Generando reporte...", Toast.LENGTH_LONG).show()

        //val fileLocation = File( this.getExternalFilesDir("/")).toString()

        val reportFile = File( this.getExternalFilesDir("/"), "Reporte_${emailEditText.text.toString()}.pdf")
        val outputStream = FileOutputStream(reportFile)
        //val fileLocation = File( this.getExternalFilesDir("/"), "nombrePDF.pdf")
        //val pdfDocument = PdfDocument(PdfWriter(reportFile))
        val writer = PdfWriter(reportFile)
        val pdfDocument = PdfDocument(writer)   // supply this instance to com.itextpdf.kernel.pdf.PdfDocument

        val document = Document(pdfDocument)    // supply this instance to create com.itextpdf.layout.Document

        val iconGctrl = getDrawable(R.drawable.ic_glucontrol)
        val bitmap = (iconGctrl as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()

        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        image.setHeight(100f)
        image.setWidth(100f)
        document.add(image)

        // This will create a table instance with weighted column width
        val table = Table(UnitValue.createPercentArray(floatArrayOf(8f, 23f, 15f, 15f, 12f, 12f, 15f))).useAllAvailableWidth()

        //Add Header Cells
        table.addHeaderCell(Cell().add(Paragraph("Date").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Job Name").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Job Size").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Job Type").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Quantity").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Rate").setTextAlignment(TextAlignment.CENTER)))
        table.addHeaderCell(Cell().add(Paragraph("Amount").setTextAlignment(TextAlignment.CENTER)))

        /*
        for (entry in entries) {
            table.addCell(Cell().add(Paragraph(shortDateFormat.format(entry.createdOn)).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(entry.getJobName())
            table.addCell(Cell().add(Paragraph(entry.jobSize).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(entry.getJobType().replace("Pouch", "")).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(entry.quantity).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(entry.rate).setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(entry.amount).setTextAlignment(TextAlignment.RIGHT)))
        }
         */

        document.add(table)

        document.close()








        /*
        Toast.makeText(this, "Generando reporte...", Toast.LENGTH_LONG).show()

        //val fileLocation = File( this.getExternalFilesDir("/")).toString()

        val reportFile = File( this.getExternalFilesDir("/"), "nombrePDF.pdf")
        val outputStream = FileOutputStream(reportFile)
        //val fileLocation = File( this.getExternalFilesDir("/"), "nombrePDF.pdf")
        //val pdfDocument = PdfDocument(PdfWriter(reportFile))
        val writer = PdfWriter(reportFile)
        val pdfDocument = PdfDocument(writer)   // supply this instance to com.itextpdf.kernel.pdf.PdfDocument

        val document = Document(pdfDocument)    // supply this instance to create com.itextpdf.layout.Document


        val text = Paragraph("My Text")
        document.add(text)

        val boldText = Paragraph("My Styled Text")
        document.add(boldText)

        val sizedText = Paragraph("My Sized Text")
        sizedText.setFontSize(20.0f)
        document.add(sizedText)

        val coloredText = Paragraph("My Sized Text")
        coloredText.setFontColor(ColorConstants.RED)
        document.add(coloredText)

        val alignedText = Paragraph("My Sized Text")
        alignedText.setTextAlignment(TextAlignment.CENTER)
        document.add(alignedText)

        document.close()
         */

    }
     */


    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



    /*
    TEST PDF GENERATION
     */




}