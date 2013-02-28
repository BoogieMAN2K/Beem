/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/victorhugogil/Documents/Eclipse Workspace/Login/src/com/beem/project/beem/service/aidl/IPrivacyListListener.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IPrivacyListListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IPrivacyListListener
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IPrivacyListListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IPrivacyListListener interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IPrivacyListListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IPrivacyListListener))) {
return ((com.beem.project.beem.service.aidl.IPrivacyListListener)iin);
}
return new com.beem.project.beem.service.aidl.IPrivacyListListener.Stub.Proxy(obj);
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
case TRANSACTION_updatedPrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.updatedPrivacyList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setPrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.beem.project.beem.service.PrivacyListItem> _arg1;
_arg1 = data.createTypedArrayList(com.beem.project.beem.service.PrivacyListItem.CREATOR);
this.setPrivacyList(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IPrivacyListListener
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
@Override public void updatedPrivacyList(java.lang.String listName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
mRemote.transact(Stub.TRANSACTION_updatedPrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setPrivacyList(java.lang.String listName, java.util.List<com.beem.project.beem.service.PrivacyListItem> listItem) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
_data.writeTypedList(listItem);
mRemote.transact(Stub.TRANSACTION_setPrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_updatedPrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setPrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void updatedPrivacyList(java.lang.String listName) throws android.os.RemoteException;
public void setPrivacyList(java.lang.String listName, java.util.List<com.beem.project.beem.service.PrivacyListItem> listItem) throws android.os.RemoteException;
}
