package chapter8.example2;

import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static void main() {
        String oldName = "oldName";
        String newName = "newName";

        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);

        atomicReference.set("unexceptedName");
        if (atomicReference.compareAndSet(oldName, newName)) {
            System.out.println("Name was updated to: " + atomicReference.get());
        } else {
            System.out.println("Name update failed. Current name is: " + atomicReference.get());
        }
    }
}
