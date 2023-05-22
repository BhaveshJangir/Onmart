package com.example.onmart.ui.Cart;

import static android.content.Context.MODE_PRIVATE;
import static android.os.ParcelFileDescriptor.MODE_APPEND;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onmart.Database.OderEntity;
import com.example.onmart.Database.OrderDatabase;
import com.example.onmart.R;
import com.example.onmart.ui.Description;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartFragment extends Fragment implements PaymentResultListener {

   RecyclerView recyclerView;
   CartAdapter cartAdapter;
   List<OderEntity> arrayList;
   OrderDatabase orderDatabase;
   TextView totalAmount;
   Button checkOut;

    Order order1;
    String id;
    int totalPrice;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        orderDatabase = OrderDatabase.getDb(getContext());
        arrayList = orderDatabase.oderDao().getOrderData();

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//        Toast.makeText(getActivity(), orderDatabase.oderDao().getOrderData().get(2).getId()+"", Toast.LENGTH_SHORT).show();
//        System.out.println(arrayList.get(2).getName()+"");

        cartAdapter = new CartAdapter(getActivity());
        recyclerView.setAdapter(cartAdapter);

        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh;
        sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);

       // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String s1 = sh.getString("name", "");
         totalPrice = sh.getInt("totalPrice", 0);
         System.out.println(totalPrice+""+s1+"lsfjlsjflsdjfl");
        Toast.makeText(getContext(), ""+totalPrice ,Toast.LENGTH_SHORT).show();
      // We can then use the data


       totalAmount = root.findViewById(R.id.total_amount);
       checkOut = root.findViewById(R.id.checkout);
       totalAmount.setText("₹"+totalPrice);


        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());


        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //region service
                service.execute(new Runnable() {
                    @Override
                    public void run() {

                        startThread();


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //   getdata();
                                startPayment();
                                orderDatabase.oderDao().truncateOrders();


                                // Storing data into SharedPreferences
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);

                               // Creating an Editor object to edit(write to the file)
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                 // Storing the key and its value as the data fetched from edittext
                                myEdit.putString("name", "bhavesh");
                                myEdit.putInt("totalPrice", 0);

                               // Once the changes have been made,
                               // we need to commit to apply those changes made,
                               // otherwise, it will throw an error
                                myEdit.commit();

                            }
                        });

                    }
                });
                //endregion
            }
        });


        return root;





    }


    public void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_0Iki94Dm2P8ohX");
        /**
         * Instantiate Checkout
         */
        //  Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        //  checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            options.put("name", "OnMart pvt ltd");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "100");
            options.put("order_id", id);//from response of step 3.
            JSONObject preFill = new JSONObject();
            preFill.put("email", "bhavdsh@gmail.com");
            preFill.put("contact", "7727064399");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }


    }
    private void startThread() {
        try{
            RazorpayClient razorpay = new RazorpayClient("rzp_test_0Iki94Dm2P8ohX", "0O7tYpHVcWHssrvy5aubg3t4");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount",totalPrice*100); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");


            order1 = razorpay.orders.create(orderRequest);
            JSONObject jsonObject = new JSONObject(String.valueOf(order1));
            id = jsonObject.getString("id");
            System.out.println(""+order1);
        } catch (
                RazorpayException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getContext(), "payment dong"+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getContext(),"failed\n"+i+"\n"+s,Toast.LENGTH_SHORT).show();
    }
}