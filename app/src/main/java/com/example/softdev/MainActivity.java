package com.example.softdev;

import static android.text.TextUtils.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

class Node {
    double data;
    Node next;

    Node(double data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedList {
    private Node head;

    LinkedList() {
        this.head = null;
    }

    // Method to add a new node with given data at the end of the list
    public void add(double data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    // Method to delete a node at a specified index
    public void deleteNodeAt(int index) {
        if (head == null) {
            return;
        }

        if (index == 0) {
            head = head.next;
            return;
        }

        Node temp = head;
        Node prev = null;
        int count = 0;

        while (temp != null) {
            if (count == index) {
                prev.next = temp.next;
                return;
            }
            prev = temp;
            temp = temp.next;
            count++;
        }

    }

    // Method to calculate the sum of all values in the list
    public double sumOfValues() {
        Node temp = head;
        double sum = 0.0;

        while (temp != null) {
            sum += temp.data;
            temp = temp.next;
        }

        return sum;
    }
}

public class MainActivity extends AppCompatActivity {
    LinkedList myList = new LinkedList();

    Button add;
    AlertDialog dialog;
    LinearLayout layout;


    private View exampleSlotView;
    private int cardIndex = 0;
    boolean initialSlotReplaced = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //For the new window
        Button summary = findViewById(R.id.summary);
        summary.setOnClickListener(v -> openActivity2());

        //For building dialog
        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        // Build the dialog
        buildDialog();

        // Check if the initial slot has been replaced
        if (!initialSlotReplaced) {
            addExampleSlot();
        }


        add.setOnClickListener(v -> {
            initialSlotReplaced = true;
            dialog.show();
        });
    }
    public void openActivity2(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    public void writeTotal(Double t_d){
        TextView viewT_D = findViewById(R.id.total_daily);
        TextView viewT_M = findViewById(R.id.total_monthly);

        myList.add(t_d);
        double sum = myList.sumOfValues();

        viewT_D.setText(String.format("%.2f",sum));
        viewT_M.setText(String.format("%.2f",sum*31));
    }

    public void writeTotal(){
        TextView viewT_D = findViewById(R.id.total_daily);
        TextView viewT_M = findViewById(R.id.total_monthly);

        double sum = myList.sumOfValues();

        viewT_D.setText(String.format("%.2f",sum));
        viewT_M.setText(String.format("%.2f",sum*31));
    }
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);
        final EditText load = view.findViewById(R.id.loadEdit);
        final EditText quantity = view.findViewById(R.id.quantityEdit);
        final EditText hrsDaily = view.findViewById(R.id.hrsDailyEdit);

        builder.setView(view);
        builder.setTitle("Enter Details");
        builder.setPositiveButton("OK", (dialog, which) -> {
            String nameStr = name.getText().toString();
            String loadStr = load.getText().toString();
            String quantityStr = quantity.getText().toString();
            String hrsDailyStr = hrsDaily.getText().toString();

            if (isEmpty(nameStr) || isEmpty(loadStr) || isEmpty(quantityStr) || isEmpty(hrsDailyStr)) {
                Toast.makeText(MainActivity.this, "Please input details", Toast.LENGTH_SHORT).show();}
            else if (isEmpty(nameStr)) {
                Toast.makeText(MainActivity.this, "Please input Device name", Toast.LENGTH_SHORT).show();
            } else if (isEmpty(loadStr) || isEmpty(quantityStr) || isEmpty(hrsDailyStr)){
                Toast.makeText(MainActivity.this, "Please input valid values", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Attempt to convert input to numbers
                    Double.parseDouble(loadStr);
                    Double.parseDouble(quantityStr);
                    Double.parseDouble(hrsDailyStr);

                    // Adding card if conversion is successful
                    addCard(nameStr, loadStr, quantityStr, hrsDailyStr);
                } catch (NumberFormatException e) {
                    // Show a toast if conversion to numbers fails
                    Toast.makeText(MainActivity.this, "Please input valid values", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });

        dialog = builder.create();
    }
    private void addCard(String name, String load, String quantity, String hrsDaily) {
        removeExampleSlot();

        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView loadView = view.findViewById(R.id.load);
        TextView quantityView = view.findViewById(R.id.quantity);
        TextView hrsDailyView = view.findViewById(R.id.hrsDaily);

        TextView viewD = view.findViewById(R.id.costDaily);
        TextView viewM = view.findViewById(R.id.costMonthly);
        Button delete = view.findViewById(R.id.delete);

        int currentIndex = cardIndex;
        ++cardIndex;

        nameView.setText(name);
        loadView.setText(load);
        quantityView.setText(quantity);
        hrsDailyView.setText(hrsDaily);

        double l = Double.parseDouble(String.valueOf(load));
        double q = Double.parseDouble(String.valueOf(quantity));
        double h = Double.parseDouble(String.valueOf(hrsDaily));
        double computedDaily = 10 * l / 1000 * q * h;
        double computedMonthly = computedDaily * 31;

        writeTotal(computedDaily);

        viewD.setText(String.format("%.2f",computedDaily));
        viewM.setText(String.format("%.2f",computedMonthly));

        delete.setOnClickListener(v -> {
            layout.removeView(view);
            myList.deleteNodeAt(currentIndex); // Pass the index of the deleted card
            --cardIndex;
            writeTotal();
        });

        layout.addView(view);
    }

    // EXAMPLES BELOW
    //DELETE EXAMPLE SLOT
    private void removeExampleSlot() {
        // Remove the example slot from the layout
        if (exampleSlotView != null) {
            layout.removeView(exampleSlotView);
            exampleSlotView = null;
        }
    }

    //CREATE EXAMPLE SLOT
    private View createAndAddSlot(String name, String load, String quantity, String hrsDaily) {
        View exampleSlotView = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = exampleSlotView.findViewById(R.id.name);
        TextView loadView = exampleSlotView.findViewById(R.id.load);
        TextView quantityView = exampleSlotView.findViewById(R.id.quantity);
        TextView hrsDailyView = exampleSlotView.findViewById(R.id.hrsDaily);
        TextView viewD = exampleSlotView.findViewById(R.id.costDaily);
        TextView viewM = exampleSlotView.findViewById(R.id.costMonthly);


        nameView.setText(name);
        loadView.setText(load);
        quantityView.setText(quantity);
        hrsDailyView.setText(hrsDaily);

        double l = Double.parseDouble(String.valueOf(load));
        double q = Double.parseDouble(String.valueOf(quantity));
        double h = Double.parseDouble(String.valueOf(hrsDaily));
        double computedDaily = 11.85 * l / 1000 * q * h;
        double computedMonthly = computedDaily * 31;


        viewD.setText(String.format("%.2f",computedDaily));
        viewM.setText(String.format("%.2f",computedMonthly));

        layout.addView(exampleSlotView);
        return exampleSlotView;
    }

    private void addExampleSlot() {
        // Create an example slot with initial data
        String exampleName = "Example Device";
        String exampleLoad = "100";
        String exampleQuantity = "2";
        String exampleHrsDaily = "4";

        // Add the example slot to the layout
        exampleSlotView = createAndAddSlot(exampleName, exampleLoad, exampleQuantity, exampleHrsDaily);
    }
}

