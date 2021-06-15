package com.tfg.campus.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.campus.Login;
import com.tfg.campus.R;
import com.tfg.campus.model.Order;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Shop extends AppCompatActivity {

    //  Establece la fecha del pedido
    Date date = new Date();
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    String day = String.valueOf(date.getDate());
    String month = String.valueOf(localDate.getMonthValue());
    String year = String.valueOf(localDate.getYear());
    String finalDate = day + "/" + month + "/" + year;

    TextView title;
    FloatingActionButton cart;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private HashMap <String, Integer> itemsInCart = new HashMap <String, Integer>();
    private float price;
    private List<Order> listOrders = new ArrayList<>();
    private String list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        //  BBDD
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //  TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //  ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        cart = findViewById(R.id.cart);

        //  TITULO DE LA ACTIVIDAD
        title.setText("Tienda");

        searchAllOrders();
    }

    //  Aniade los items al carrito
    private void addCart(String item){
        if (itemsInCart.containsKey(item)){
            itemsInCart.put(item, itemsInCart.get(item)+1);
        }else{
            itemsInCart.put(item,1);
        }
    }

    //  Asociacion de la descripcion con cada imagen
    public void imgCleanService(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Servicio de Limpieza")
                .setMessage("El servicio incluye una exhaustiva limpieza (barrido, fregado y eliminación de polvo.\n" + "Precio: 8€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Servicio Limpieza");
                        price+=8;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgLaundryService(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Servicio de Lavandería")
                .setMessage("El servicio incluye un lavado de 8Kg y secado rápido.\n" + "Precio: 5€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Servicio Lavandería");
                        price+=5;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgAspirateService(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Servicio de Aspirado")
                .setMessage("El servicio incluye el aspirado de todas las alfombras, camas y otras zonas accesibles.\n" + "Precio: 6€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Servicio Aspirado");
                        price+=6;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgBrownWallet(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Cartera Marrón")
                .setMessage("La cartera está fabricada en Itlia con las mejores calidades.\n" + "Precio: 15€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Cartera Marrón");
                        price+=15;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgBlueBag(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Mochila Azul")
                .setMessage("Esta mochila diseñada en Francia ofrece compartimentos interiores para todos sus complementos.\n" + "Precio: 10€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Mochila Azul");
                        price+=10;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgBlackBag(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Mochila Negra")
                .setMessage("Mochila con un estilo más urbano, ligera y facil de transportar.\n" + "Precio: 7€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Mochila Negra");
                        price+=7;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgPinkWallet(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Tarjetero Rosa")
                .setMessage("Una cartera de lo más llamativa. Permite guardar hasta 8 tarjetas en su interior.\n" + "Precio: 6€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Tarjetero Rosa");
                        price+=6;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgSingleSpace(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Espacio Individual")
                .setMessage("Espacio perfecto para estudiantes que requieren de un lugar adicional totalmente insonorizado.\n" + "Precio: 15€/día")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Espacio Individual");
                        price+=15;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgMeetingRoomInformal(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sala de Reuniones Informal")
                .setMessage("Espacio idóneo para grupos de personas de un máximo de 5 personas.\n" + "Precio: 25€/día")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Sala Reunión Informal");
                        price+=25;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgMeetingRoomSmall(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sala de Reuniones Pequeña")
                .setMessage("Esta sala cuenta con equipamiento tecnológico.\nSu capacidad máxima es de 10 personas.\n" + "Precio: 55€/día")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Sala Reunión Pequeña");
                        price+=55;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgMeetingRoomSimple(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sala de Reuniones Simple")
                .setMessage("Esta sala cuenta con insonorización completa. Ideal para 9 personas.\n" + "Precio: 45€/día")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Sala Reunión Simple");
                        price+=45;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgMaterialCompass(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Brújula")
                .setMessage("Útil invento para orientarse. Recomendada para los estudiantes de turismo.\n" + "Precio: 6€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Brújula");
                        price+=6;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgPencilNotebook(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Lápiz y Cuaderno")
                .setMessage("Este simple pack ofrece a los escritores la oportunidad de plasmar su arte en un papel de la mejor calidad.\nSe dice que la tinta de este bolígrafo potencia la imaginación...\n" + "Precio: 15€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Pack Lapiz/Cuaderno");
                        price+=15;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgMask(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Mascarillas")
                .setMessage("Mascarillas testadas.\nProtegete y protege.\n" + "Precio: 0.4€/unidad")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Mascarilla");
                        price+=0.4;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgHat(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sombrero")
                .setMessage("Moda especial para los residentes más exigentes.\n¡Ni un pelo al sol!\n" + "Precio: 5€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Sombrero");
                        price+=5;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgTie(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Corbata")
                .setMessage("Corbata elegante.\nTanta elegancia para +18 años.\n" + "Precio: 7€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Corbata");
                        price+=7;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgTrousers(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Pantalones")
                .setMessage("Talla única.\nAptos para cuerpos con muchas curvas.\n" + "Precio: 9€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Pantalones");
                        price+=9;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void imgShoes(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Deportivas")
                .setMessage("Unas deportivas y a correr.\nLa moda no entiende de géneros.\n" + "Precio: 15€")
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        addCart("Deportivas");
                        price+=15;
                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    //  Busca todas las ordenes y las lista
    private void searchAllOrders(){
        myRef.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listOrders.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Order o = objSnaptshot.getValue(Order.class);
                    listOrders.add(o);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //  Guarda la orden en la BBDD
    private void saveOrder(){
        Order order;
        int num = 0, finalNum = 0;
        for (Order o : listOrders){ //  Buscamos el mayor numero en la lista de ordenes para posicionar la nueva en el siquiente mayor
            num = Integer.parseInt(o.getOid().subSequence(0,o.getOid().length()-1).toString());
            if (num > finalNum){
                finalNum = num;
            }
        }
        finalNum++;
        order = new Order (String.valueOf(price), list.substring(1, list.length()-1), finalDate, String.valueOf(finalNum) + "O", "Pagado", Login.u.getUid());
        myRef.child("orders/" + finalNum + "O").setValue(order);
        itemsInCart.clear();
        price = 0;
    }

    //  Actualiza el saldo del usuario
    private void shop(){
        String finalCash = String.valueOf(Float.parseFloat(Login.u.getCash())-price);
        Login.u.setCash(finalCash); //  Actualiza el dinero restante del usuario
        myRef.child("users/" + Login.u.getUid()).setValue(Login.u); //  Actualiaza el usuario en la BBDD

        Toast.makeText(this, "Compra Realizada", Toast.LENGTH_LONG).show();

        saveOrder();
    }

    //  Asegura que el usuario tiene los fondos suficientes o vacia el carrito
    public void showCart(View v){
        list = String.valueOf(itemsInCart.keySet());

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Carrito")
                .setMessage(list.substring(1, list.length()-1) + "\n\n- PRECIO TOTAL: " + price + "€")
                .setPositiveButton("Comprar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  Comprueba el saldo del usuario
                        if (price > Float.parseFloat(Login.u.getCash())){
                            Toast.makeText(getApplicationContext(), "Fondos Insuficientes", Toast.LENGTH_LONG).show();
                        }else if (itemsInCart.size()<1){
                            Toast.makeText(getApplicationContext(), "Sin artículos", Toast.LENGTH_LONG).show();
                        }else{
                            shop();
                        }
                    }
                })
                .setNegativeButton("Vaciar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        itemsInCart.clear();
                        price = 0;
                    }
                });
        dialog.show();
    }
}