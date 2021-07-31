package com.alfardev.dipinjamin.ui.detail_my_order

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.Checkout
import kotlinx.android.synthetic.main.activity_detail_my_order.*
import kotlinx.android.synthetic.main.content_detail_my_order.*

class DetailMyOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_my_order)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        getPassedCheckout()?.let {
            book_image.load(it.book?.image)
            if(getPassedIsOwner()){
                txt_phone.text = it.borrower?.name + " : " + it.borrower?.phone
                clickToWa(it.borrower?.phone!!)
            }else{
                txt_phone.text = it.borrower?.name + " : " + it.owner?.phone
                clickToWa(it.owner?.phone!!)
            }
            text_title_book.text = it.book?.title
            txt_writer.text = it.book?.writer
            txt_year.text = it.book?.year
            txt_category.text = it.book?.category!!.category
            txt_publisher.text = it.book?.publisher
            text_thickness.text = it.book?.number_of_pages + getString(R.string.pages)
            text_viewer.text = it.book?.viewer.toString()
            txt_sinopsis.text = it.book?.description
        }
    }

    private fun getPassedCheckout() = intent.getParcelableExtra<Checkout>("CHECKOUT")
    private fun getPassedIsOwner() = intent.getBooleanExtra("IS_OWNER", false)

    private fun clickToWa(phone : String){
        val url = "https://api.whatsapp.com/send?phone=$phone"
        txt_phone.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}