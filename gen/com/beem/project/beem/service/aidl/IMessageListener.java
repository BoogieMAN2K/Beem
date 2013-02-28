/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/victorhugogil/Documents/Eclipse Workspace/Login/src/com/beem/project/beem/service/aidl/IMessageListener.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IMessageListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IMessageListener
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IMessageListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IMessageListener interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IMessageListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IMessageListener))) {
return ((com.beem.project.beem.service.aidl.IMessageListener)iin);
}
return new com.beem.project.beem.service.aidl.IMessageListener.Stub.Proxy(obj);
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
case TRANSACTION_processMessage:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IChat _arg0;
_arg0 = com.beem.project.beem.service.aidl.IChat.Stub.asInterface(data.readStrongBinder());
com.beem.project.beem.service.Message _arg1;
if ((0!=data.readInt())) {
_arg1 = com.beem.project.beem.service.Message.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
this.processMessage(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_stateChanged:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IChat _arg0;
_arg0 = com.beem.project.beem.service.aidl.IChat.Stub.asInterface(data.readStrongBinder());
this.stateChanged(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_otrStateChanged:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.otrStateChanged(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IMessageListener
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
	 * This method is executed when a chat receive a message.
	 * @param chat the chat receiving the message.
	 * @param msg the message received in the chat.
	 */
@Override public void processMessage(com.beem.project.beem.service.aidl.IChat chat, com.beem.project.beem.service.Message msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((chat!=null))?(chat.asBinder()):(null)));
if ((msg!=null)) {
_data.writeInt(1);
msg.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_processMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * This method is executed when a new ChatState is received by the chat.
	 * You can use IChat.getState() in order to get the new state.
	 * @param chat the chat changed.
	 */
@Override public void stateChanged(com.beem.project.beem.service.aidl.IChat chat) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((chat!=null))?(chat.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_stateChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * This method is executed when the otr session status change.
	 * @param otrState the new state of otr session.
	 */
@Override public void otrStateChanged(java.lang.String otrState) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(otrState);
mRemote.transact(Stub.TRANSACTION_otrStateChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_processMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_otrStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
/**
	 * This method is executed when a chat receive a message.
	 * @param chat the chat receiving the message.
	 * @param msg the message received in the chat.
	 */
public void processMessage(com.beem.project.beem.service.aidl.IChat chat, com.beem.project.beem.service.Message msg) throws android.os.RemoteException;
/**
	 * This method is executed when a new ChatState is received by the chat.
	 * You can use IChat.getState() in order to get the new state.
	 * @param chat the chat changed.
	 */
public void stateChanged(com.beem.project.beem.service.aidl.IChat chat) throws android.os.RemoteException;
/**
	 * This method is executed when the otr session status change.
	 * @param otrState the new state of otr session.
	 */
public void otrStateChanged(java.lang.String otrState) throws android.os.RemoteException;
}
