package com.github.aakumykov.storage_access_helper.storage_access_helper

import permissions.dispatcher.ktx.PermissionsRequester

abstract class StorageAccessHelperLegacy protected constructor(resultCallback: ResultCallback)
    : StorageAccessHelper(resultCallback) {

    override fun requestReadingAccess() = readingStoragePermissionRequester.launch()
    override fun requestWritingAccess() = writingStoragePermissionRequester.launch()
    override fun requestStorageAccess() = fullStoragePermissionsRequester.launch()

    override fun hasStorageReadingAccess(): Boolean = isPermissionGranted(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    override fun hasStorageWritingAccess(): Boolean = isPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun hasStorageFullAccess(): Boolean = hasStorageReadingAccess() && hasStorageWritingAccess()


    protected fun readAccessPermission(): String = android.Manifest.permission.READ_EXTERNAL_STORAGE
    protected fun writeAccessPermission(): String = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    protected fun fullAccessPermissions(): Array<String> = arrayOf(readAccessPermission(), writeAccessPermission())

    protected abstract val readingStoragePermissionRequester: PermissionsRequester
    protected abstract val writingStoragePermissionRequester: PermissionsRequester
    protected abstract val fullStoragePermissionsRequester: PermissionsRequester

    protected abstract fun isPermissionGranted(permission: String): Boolean
}