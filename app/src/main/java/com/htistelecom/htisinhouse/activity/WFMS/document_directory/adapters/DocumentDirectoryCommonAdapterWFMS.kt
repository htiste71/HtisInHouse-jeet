package com.htistelecom.htisinhouse.activity.WFMS.document_directory.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.htistelecom.htisinhouse.R
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.DocumentDirectoryFragmentWFMS
import com.htistelecom.htisinhouse.activity.WFMS.document_directory.models.DocumentListModelWFMS
import kotlinx.android.synthetic.main.row_document_directory_common_adapter_wfms.view.*


class DocumentDirectoryCommonAdapterWFMS(val ctx: Context, val documentList: ArrayList<DocumentListModelWFMS>, val type: Int,val frag: DocumentDirectoryFragmentWFMS) : RecyclerView.Adapter<DocumentDirectoryCommonAdapterWFMS.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_document_directory_common_adapter_wfms, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    override fun onBindViewHolder(holdr: MyHolder, pos: Int) {

        holdr.bind(frag,type, documentList.get(pos))
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(frag:DocumentDirectoryFragmentWFMS,type: Int, model: DocumentListModelWFMS) {
            if (type == 0) {
                //Requested
                itemView.llDefaultDocument.visibility = View.GONE
                itemView.llUploadedDoc.visibility = View.GONE


                itemView.llRequestedDocument.visibility = View.VISIBLE

                itemView.tvDateRequestedDocWFMS.text = model.docRequestDate
                itemView.tvDocumentTypeRequestedDocWFMS.text = model.docType
                itemView.tvRequestToRequestedDocWFMS.text = model.reqToDept
                if (model.reqStatus.equals("Pending", true)) {
                    itemView.tvStatusRequestedDocWFMS.visibility=View.VISIBLE
                    itemView.tvStatusRequestedDocWFMS.text=model.reqStatus
                    itemView.btnDownloadRequestedDocWFMS.visibility=View.GONE
                } else {
                    itemView.tvStatusRequestedDocWFMS.visibility=View.GONE
                    itemView.btnDownloadRequestedDocWFMS.visibility=View.VISIBLE
                }
            } else if (type == 1) {
                //Default
                itemView.llDefaultDocument.visibility = View.VISIBLE
                itemView.llRequestedDocument.visibility = View.GONE
                itemView.llUploadedDoc.visibility = View.GONE
                  itemView.tvDateDefaultDoc.text = model.docRequestDate
                itemView.tvDocumentTypeDefaultDoc.text = model.docType
                itemView.btnDownloadDefaultDoc.setOnClickListener { view ->

                }

            } else if (type == 2) {
                //Uploaded
                itemView.llDefaultDocument.visibility = View.GONE
                itemView.llRequestedDocument.visibility = View.GONE
                itemView.llUploadedDoc.visibility = View.VISIBLE
                itemView.tvUploadDateUploadedDoc.text = model.docRequestDate
                itemView.tvDocumentTypeUploadedDoc.text = model.docType
                itemView.tvSendToUploadedDoc.text = model.reqToDept
                if (model.reqStatus.equals("Pending", true)) {
                    itemView.ivUploadDocument.visibility = View.VISIBLE
                } else {
                    itemView.ivUploadDocument.visibility = View.GONE

                }
                itemView.tvStatusUploadedDoc.text = model.reqStatus

                itemView.ivUploadDocument.setOnClickListener {
                    view ->frag.dialogUploadDoc(model.docRequestId,model.docType)
                }


            }

        }


    }
}
