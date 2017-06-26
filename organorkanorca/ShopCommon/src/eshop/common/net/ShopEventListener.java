package eshop.common.net;

import java.rmi.Remote;

import eshop.common.data_objects.Artikel;

public interface ShopEventListener extends Remote {

    public void handleArticleChanged(Artikel art);

    public void handleEventChanged();
    // public void handleServerStatusChange();

    public void handleUserChanged();
}
