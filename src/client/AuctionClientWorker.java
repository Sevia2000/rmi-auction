package client;

import server.Bid;
import server.IAuctionServer;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

/**
 * Created by justas on 07/11/16.
 */
public class AuctionClientWorker implements Runnable, IAuctionClient {

    private IAuctionServer auctionSrv;
    private String name;

    public AuctionClientWorker(IAuctionServer auctionSrv) {
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(
                "John", "Alex", "Bender", "Jess", "Chris", "Alberto", "Xin", "Jack",
                "Spencer", "Mark", "Lorenzo", "Peter", "Miranda"));
        this.name = names.get(new Random().nextInt(names.size()));
        this.auctionSrv = auctionSrv;
    }

    @Override
    public void run() {
        Random rg = new Random();
        //int timer = rg.nextInt(5000);

        this.name = this.name + " " + Thread.currentThread().getName();

        System.out.println(this.name + " is running");

        try {
            for (String line: auctionSrv.getOpenAuctions().split("\n")) {
                float guess = rg.nextFloat();
                // We only want to bid on half of the items
                // and we're only interested in the line that contains #ID:, e.g. #10:
                if (guess > 0.5f && line.matches("#([0-9])+:")) {
                    int id = Integer.valueOf(line.split("#")[1].split(":")[0]);
                    auctionSrv.bid(this, id, 10 + 100 * rg.nextFloat());
                }
            }
        } catch (RemoteException e) {
            System.err.println("Could not contact the server " + e);
            System.exit(1);
        }

        System.out.println();
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO: Implement
    }
}