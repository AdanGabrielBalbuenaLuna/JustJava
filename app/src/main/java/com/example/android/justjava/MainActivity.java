package com.example.android.justjava;
/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity; Comment this line
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//Add this code line
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2; //Declaration of global Variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Figure out if the user wants Whipped Cream
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.topping_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        //--Log.v("MainActivity", "Current Checkbox state: " + hasWhippedCream);

        // Figure out if the user wants chocolate topping
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // Get the customer's name
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        String customerName = nameEditText.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        //--Log example Log.v("MainActivity","The price is = " + price);

        String message = createOrderSumary(customerName, price, hasWhippedCream, hasChocolate);
        //displayMessage(message);


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, customerName));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * Create the sumary of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not the user wants chocolate topping
     * @param price of the order
     * @return text summary
     */
    private String createOrderSumary(String customerName, int price, boolean addWhippedCream, boolean addChocolate){
        String priceMessage = getString(R.string.jv_txt_name, customerName);
        priceMessage += "\n" + getString(R.string.jv_txt_add_whipped_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.jv_txt_add_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.jv_txt_quantity, quantity);
        priceMessage += "\n" + getString(R.string.jv_txt_total,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.jv_txt_thank_you);
        return priceMessage;
    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not the user wants chocolate topping
     * @return Total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate){
        //Price of one Cup of coffee
        int basePrice = 5;

        //Add $1 if the user wants whipped cream
        if (addWhippedCream){
            basePrice = basePrice + 1;
        }

        //Add $2 if the user wants chocolate
        if (addChocolate){
            basePrice = basePrice + 2;
        }

        //Calculate the total order price by multiplying by quantity
        return quantity * basePrice;
    }


    /**
     * This method increase the amount of coffees on screen.
     */
    public void increment(View view){
        if(quantity==100){
            //Show an error message as Toast
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show(); //Toast object
            //Exit this method early, there's nothing else to do
            return;
        }
        quantity = quantity+1;
        displayQuantity(quantity);
    }

    /**
     * This method decrease the amount of coffes on scrreen.
     */
    public void decrement(View view){
        if(quantity==1){
            //Show an error message as Toast
            Toast.makeText(this, "You cannot have less than 1 coffees", Toast.LENGTH_SHORT).show(); //Toast object
            //Exit this method early, there's nothing else to do
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }
}
