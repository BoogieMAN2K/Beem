/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/victorhugogil/Documents/Eclipse Workspace/Login/src/com/beem/project/beem/service/aidl/IPrivacyListManager.aidl
 */
package com.beem.project.beem.service.aidl;
public interface IPrivacyListManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.beem.project.beem.service.aidl.IPrivacyListManager
{
private static final java.lang.String DESCRIPTOR = "com.beem.project.beem.service.aidl.IPrivacyListManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.beem.project.beem.service.aidl.IPrivacyListManager interface,
 * generating a proxy if needed.
 */
public static com.beem.project.beem.service.aidl.IPrivacyListManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.beem.project.beem.service.aidl.IPrivacyListManager))) {
return ((com.beem.project.beem.service.aidl.IPrivacyListManager)iin);
}
return new com.beem.project.beem.service.aidl.IPrivacyListManager.Stub.Proxy(obj);
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
case TRANSACTION_createPrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.beem.project.beem.service.PrivacyListItem> _arg1;
_arg1 = data.createTypedArrayList(com.beem.project.beem.service.PrivacyListItem.CREATOR);
this.createPrivacyList(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_removePrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.removePrivacyList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_editPrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<com.beem.project.beem.service.PrivacyListItem> _arg1;
_arg1 = data.createTypedArrayList(com.beem.project.beem.service.PrivacyListItem.CREATOR);
this.editPrivacyList(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getActivePrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getActivePrivacyList();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getDefaultPrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getDefaultPrivacyList();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setActivePrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setActivePrivacyList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setDefaultPrivacyList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setDefaultPrivacyList(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_declineActivePrivacyList:
{
data.enforceInterface(DESCRIPTOR);
this.declineActivePrivacyList();
reply.writeNoException();
return true;
}
case TRANSACTION_declineDefaultPrivacyList:
{
data.enforceInterface(DESCRIPTOR);
this.declineDefaultPrivacyList();
reply.writeNoException();
return true;
}
case TRANSACTION_getPrivacyLists:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.getPrivacyLists();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_blockUser:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.blockUser(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getBlockedUsersByList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<java.lang.String> _result = this.getBlockedUsersByList(_arg0);
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_getBlockedGroupsByList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.List<java.lang.String> _result = this.getBlockedGroupsByList(_arg0);
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_addPrivacyListListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IPrivacyListListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IPrivacyListListener.Stub.asInterface(data.readStrongBinder());
this.addPrivacyListListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removePrivacyListListener:
{
data.enforceInterface(DESCRIPTOR);
com.beem.project.beem.service.aidl.IPrivacyListListener _arg0;
_arg0 = com.beem.project.beem.service.aidl.IPrivacyListListener.Stub.asInterface(data.readStrongBinder());
this.removePrivacyListListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.beem.project.beem.service.aidl.IPrivacyListManager
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
@Override public void createPrivacyList(java.lang.String listName, java.util.List<com.beem.project.beem.service.PrivacyListItem> items) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
_data.writeTypedList(items);
mRemote.transact(Stub.TRANSACTION_createPrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removePrivacyList(java.lang.String listName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
mRemote.transact(Stub.TRANSACTION_removePrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void editPrivacyList(java.lang.String listName, java.util.List<com.beem.project.beem.service.PrivacyListItem> items) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
_data.writeTypedList(items);
mRemote.transact(Stub.TRANSACTION_editPrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String getActivePrivacyList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getActivePrivacyList, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getDefaultPrivacyList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDefaultPrivacyList, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setActivePrivacyList(java.lang.String listName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
mRemote.transact(Stub.TRANSACTION_setActivePrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setDefaultPrivacyList(java.lang.String listName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
mRemote.transact(Stub.TRANSACTION_setDefaultPrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void declineActivePrivacyList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_declineActivePrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void declineDefaultPrivacyList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_declineDefaultPrivacyList, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<java.lang.String> getPrivacyLists() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPrivacyLists, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void blockUser(java.lang.String listName, java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_blockUser, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<java.lang.String> getBlockedUsersByList(java.lang.String listName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
mRemote.transact(Stub.TRANSACTION_getBlockedUsersByList, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<java.lang.String> getBlockedGroupsByList(java.lang.String listName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(listName);
mRemote.transact(Stub.TRANSACTION_getBlockedGroupsByList, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void addPrivacyListListener(com.beem.project.beem.service.aidl.IPrivacyListListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addPrivacyListListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removePrivacyListListener(com.beem.project.beem.service.aidl.IPrivacyListListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removePrivacyListListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_createPrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_removePrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_editPrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getActivePrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getDefaultPrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setActivePrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setDefaultPrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_declineActivePrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_declineDefaultPrivacyList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getPrivacyLists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_blockUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getBlockedUsersByList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getBlockedGroupsByList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_addPrivacyListListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_removePrivacyListListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
}
public void createPrivacyList(java.lang.String listName, java.util.List<com.beem.project.beem.service.PrivacyListItem> items) throws android.os.RemoteException;
public void removePrivacyList(java.lang.String listName) throws android.os.RemoteException;
public void editPrivacyList(java.lang.String listName, java.util.List<com.beem.project.beem.service.PrivacyListItem> items) throws android.os.RemoteException;
public java.lang.String getActivePrivacyList() throws android.os.RemoteException;
public java.lang.String getDefaultPrivacyList() throws android.os.RemoteException;
public void setActivePrivacyList(java.lang.String listName) throws android.os.RemoteException;
public void setDefaultPrivacyList(java.lang.String listName) throws android.os.RemoteException;
public void declineActivePrivacyList() throws android.os.RemoteException;
public void declineDefaultPrivacyList() throws android.os.RemoteException;
public java.util.List<java.lang.String> getPrivacyLists() throws android.os.RemoteException;
public void blockUser(java.lang.String listName, java.lang.String jid) throws android.os.RemoteException;
public java.util.List<java.lang.String> getBlockedUsersByList(java.lang.String listName) throws android.os.RemoteException;
public java.util.List<java.lang.String> getBlockedGroupsByList(java.lang.String listName) throws android.os.RemoteException;
public void addPrivacyListListener(com.beem.project.beem.service.aidl.IPrivacyListListener listener) throws android.os.RemoteException;
public void removePrivacyListListener(com.beem.project.beem.service.aidl.IPrivacyListListener listener) throws android.os.RemoteException;
}
