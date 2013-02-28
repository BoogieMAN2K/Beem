/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/victorhugogil/Documents/Eclipse Workspace/Login/src/com/beem/project/beem/service/aidl/IBeemConnectionListener.aidl
 */
package com.beem.project.beem.service.aidl;
/**
 * Interface to listen for connection events
 * @author Da Risk <barthe_f@epitech.eu>
 */
public interface IBeemConnectionListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IBeemConnectionListener
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IBeemConnectionListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IBeemConnectionListener interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IBeemConnectionListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IBeemConnectionListener))) {
return ((com.beem.project.beem.service.aidl.IBeemConnectionListener)iin);
}
return new com.beem.project.beem.service.aidl.IBeemConnectionListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_connectionClosed:
{
data.enforceInterface(DESCRIPTOR);
this.connectionClosed();
reply.writeNoException();
return true;
}
case TRANSACTION_connectionClosedOnError:
{
data.enforceInterface(DESCRIPTOR);
this.connectionClosedOnError();
reply.writeNoException();
return true;
}
case TRANSACTION_reconnectingIn:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.reconnectingIn(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_reconnectionFailed:
{
data.enforceInterface(DESCRIPTOR);
this.reconnectionFailed();
reply.writeNoException();
return true;
}
case TRANSACTION_reconnectionSuccessful:
{
data.enforceInterface(DESCRIPTOR);
this.reconnectionSuccessful();
reply.writeNoException();
return true;
}
case TRANSACTION_connectionFailed:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.connectionFailed(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IBeemConnectionListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     *  Callback to call when the connection is closed
     */
@Override public void connectionClosed() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_connectionClosed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     *  Callback to call when the connection occurs
     *  @Deprecated
     *///void onConnect();
//void connectionClosedOnError(in Exception e);
/**
     *  Callback to call when the connection is closed on error
     */
@Override public void connectionClosedOnError() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_connectionClosedOnError, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Callback to call when trying to reconnecting
     */
@Override public void reconnectingIn(int seconds) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(seconds);
mRemote.transact(Stub.TRANSACTION_reconnectingIn, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     *  Callback to call when the reconnection has failed
     */
@Override public void reconnectionFailed() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_reconnectionFailed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     *  Callback to call when the reconnection is successfull
     */
@Override public void reconnectionSuccessful() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_reconnectionSuccessful, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     *  Callback to call when the connection Failed
     */
@Override public void connectionFailed(java.lang.String errorMsg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(errorMsg);
mRemote.transact(Stub.TRANSACTION_connectionFailed, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_connectionClosed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_connectionClosedOnError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_reconnectingIn = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_reconnectionFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_reconnectionSuccessful = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_connectionFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
/**
     *  Callback to call when the connection is closed
     */
public void connectionClosed() throws android.os.RemoteException;
/**
     *  Callback to call when the connection occurs
     *  @Deprecated
     *///void onConnect();
//void connectionClosedOnError(in Exception e);
/**
     *  Callback to call when the connection is closed on error
     */
public void connectionClosedOnError() throws android.os.RemoteException;
/**
     * Callback to call when trying to reconnecting
     */
public void reconnectingIn(int seconds) throws android.os.RemoteException;
/**
     *  Callback to call when the reconnection has failed
     */
public void reconnectionFailed() throws android.os.RemoteException;
/**
     *  Callback to call when the reconnection is successfull
     */
public void reconnectionSuccessful() throws android.os.RemoteException;
/**
     *  Callback to call when the connection Failed
     */
public void connectionFailed(java.lang.String errorMsg) throws android.os.RemoteException;
}
