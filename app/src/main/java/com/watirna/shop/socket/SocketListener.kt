package com.watirna.shop.socket

interface SocketListener {

    interface CallBack {
        fun onConnected()

        fun onDisconnected()

        fun onConnectionError()

        fun onConnectionTimeOut()
    }

    interface ConnectionRefreshCallBack {
        fun onRefresh()
    }

}