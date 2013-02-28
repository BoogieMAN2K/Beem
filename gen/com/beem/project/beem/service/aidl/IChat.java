/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/victorhugogil/Documents/Eclipse Workspace/Login/src/com/beem/project/beem/service/aidl/IChat.aidl
 */
package com.beem.project.beem.service.aidl;
/**
 * An aidl interface for Chat session.
 */
public interface IChat extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IChat
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IChat";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IChat interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IChat asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IChat))) {
return ((com.beem.project.beem.service.aidl.IChat)iin);
}
return new com.beem.project.beem.service.aidl.IChat.Stub.Proxy(obj);
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
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.Message _arg0;
if ((0!=data.readInt())) {
_arg0 = com.beem.project.beem.service.Message.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.sendMessage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getParticipant:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.Contact _result = this.getParticipant();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_addMessageListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IMessageListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IMessageListener.Stub.asInterface(data.readStrongBinder());
this.addMessageListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeMessageListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IMessageListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IMessageListener.Stub.asInterface(data.readStrongBinder());
this.removeMessageListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getState();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setOpen:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setOpen(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isOpen:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isOpen();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getUnreadMessageCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getUnreadMessageCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setState(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getMessages:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.beem.project.beem.service.Message> _result = this.getMessages();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_startOtrSession:
{
data.enforceInterface(DESCRIPTOR);
this.startOtrSession();
reply.writeNoException();
return true;
}
case TRANSACTION_endOtrSession:
{
data.enforceInterface(DESCRIPTOR);
this.endOtrSession();
reply.writeNoException();
return true;
}
case TRANSACTION_getLocalOtrFingerprint:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getLocalOtrFingerprint();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getRemoteOtrFingerprint:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getRemoteOtrFingerprint();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_verifyRemoteFingerprint:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.verifyRemoteFingerprint(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getOtrStatus:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getOtrStatus();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IChat
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
    	 * Send a message.
    	 * @param message	the message to send
    	 */
@Override public void sendMessage(com.beem.project.beem.service.Message message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((message!=null)) {
_data.writeInt(1);
message.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * Get the participant of the chat
	 * @return the participant
	 */
@Override public com.beem.project.beem.service.Contact getParticipant() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.beem.project.beem.service.Contact _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getParticipant, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = com.beem.project.beem.service.Contact.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * Add a message listener.
	 * @param listener the listener to add.
	 */
@Override public void addMessageListener(com.beem.project.beem.service.aidl.IMessageListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addMessageListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * Remove a message listener.
	 * @param listener the listener to remove.
	 */
@Override public void removeMessageListener(com.beem.project.beem.service.aidl.IMessageListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeMessageListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String getState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getState, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setOpen(boolean isOpen) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((isOpen)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setOpen, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isOpen() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isOpen, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getUnreadMessageCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUnreadMessageCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setState(java.lang.String state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(state);
mRemote.transact(Stub.TRANSACTION_setState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<com.beem.project.beem.service.Message> getMessages() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.beem.project.beem.service.Message> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMessages, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.beem.project.beem.service.Message.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * Try to start an OTR session.
	 */
@Override public void startOtrSession() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startOtrSession, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * Stop the OTR session.
	 */
@Override public void endOtrSession() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_endOtrSession, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * get local OTR key fingerprints.
	 */
@Override public java.lang.String getLocalOtrFingerprint() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLocalOtrFingerprint, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * get remote OTR key fingerprints.
	 */
@Override public java.lang.String getRemoteOtrFingerprint() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRemoteOtrFingerprint, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void verifyRemoteFingerprint(boolean ok) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((ok)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_verifyRemoteFingerprint, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * get current OTR status.
	 */
@Override public java.lang.String getOtrStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getOtrStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getParticipant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_addMessageListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_removeMessageListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setOpen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_isOpen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getUnreadMessageCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_setState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getMessages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_startOtrSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_endOtrSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getLocalOtrFingerprint = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getRemoteOtrFingerprint = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_verifyRemoteFingerprint = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getOtrStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
}
/**
    	 * Send a message.
    	 * @param message	the message to send
    	 */
public void sendMessage(com.beem.project.beem.service.Message message) throws android.os.RemoteException;
/**
	 * Get the participant of the chat
	 * @return the participant
	 */
public com.beem.project.beem.service.Contact getParticipant() throws android.os.RemoteException;
/**
	 * Add a message listener.
	 * @param listener the listener to add.
	 */
public void addMessageListener(com.beem.project.beem.service.aidl.IMessageListener listener) throws android.os.RemoteException;
/**
	 * Remove a message listener.
	 * @param listener the listener to remove.
	 */
public void removeMessageListener(com.beem.project.beem.service.aidl.IMessageListener listener) throws android.os.RemoteException;
public java.lang.String getState() throws android.os.RemoteException;
public void setOpen(boolean isOpen) throws android.os.RemoteException;
public boolean isOpen() throws android.os.RemoteException;
public int getUnreadMessageCount() throws android.os.RemoteException;
public void setState(java.lang.String state) throws android.os.RemoteException;
public java.util.List<com.beem.project.beem.service.Message> getMessages() throws android.os.RemoteException;
/**
	 * Try to start an OTR session.
	 */
public void startOtrSession() throws android.os.RemoteException;
/**
	 * Stop the OTR session.
	 */
public void endOtrSession() throws android.os.RemoteException;
/**
	 * get local OTR key fingerprints.
	 */
public java.lang.String getLocalOtrFingerprint() throws android.os.RemoteException;
/**
	 * get remote OTR key fingerprints.
	 */
public java.lang.String getRemoteOtrFingerprint() throws android.os.RemoteException;
public void verifyRemoteFingerprint(boolean ok) throws android.os.RemoteException;
/**
	 * get current OTR status.
	 */
public java.lang.String getOtrStatus() throws android.os.RemoteException;
}
