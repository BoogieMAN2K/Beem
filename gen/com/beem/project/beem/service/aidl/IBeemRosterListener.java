/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/victorhugogil/Documents/Eclipse Workspace/Login/src/com/beem/project/beem/service/aidl/IBeemRosterListener.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IBeemRosterListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IBeemRosterListener
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IBeemRosterListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IBeemRosterListener interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IBeemRosterListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IBeemRosterListener))) {
return ((com.beem.project.beem.service.aidl.IBeemRosterListener)iin);
}
return new com.beem.project.beem.service.aidl.IBeemRosterListener.Stub.Proxy(obj);
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
case TRANSACTION_onEntriesAdded:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _arg0;
_arg0 = data.createStringArrayList();
this.onEntriesAdded(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onEntriesUpdated:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _arg0;
_arg0 = data.createStringArrayList();
this.onEntriesUpdated(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onEntriesDeleted:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _arg0;
_arg0 = data.createStringArrayList();
this.onEntriesDeleted(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPresenceChanged:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.PresenceAdapter _arg0;
if ((0!=data.readInt())) {
_arg0 = com.beem.project.beem.service.PresenceAdapter.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.onPresenceChanged(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IBeemRosterListener
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
@Override public void onEntriesAdded(java.util.List<java.lang.String> addresses) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStringList(addresses);
mRemote.transact(Stub.TRANSACTION_onEntriesAdded, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onEntriesUpdated(java.util.List<java.lang.String> addresses) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStringList(addresses);
mRemote.transact(Stub.TRANSACTION_onEntriesUpdated, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onEntriesDeleted(java.util.List<java.lang.String> addresses) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStringList(addresses);
mRemote.transact(Stub.TRANSACTION_onEntriesDeleted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPresenceChanged(com.beem.project.beem.service.PresenceAdapter presence) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((presence!=null)) {
_data.writeInt(1);
presence.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onPresenceChanged, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onEntriesAdded = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onEntriesUpdated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onEntriesDeleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onPresenceChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void onEntriesAdded(java.util.List<java.lang.String> addresses) throws android.os.RemoteException;
public void onEntriesUpdated(java.util.List<java.lang.String> addresses) throws android.os.RemoteException;
public void onEntriesDeleted(java.util.List<java.lang.String> addresses) throws android.os.RemoteException;
public void onPresenceChanged(com.beem.project.beem.service.PresenceAdapter presence) throws android.os.RemoteException;
}
