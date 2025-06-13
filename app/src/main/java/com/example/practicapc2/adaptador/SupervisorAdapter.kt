package com.example.practicapc2.adaptador

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practicapc2.MainActivity
import com.example.practicapc2.R
import com.example.practicapc2.detalle_supervisor
import com.example.practicapc2.entidad.Supervisores
import java.io.File

class SupervisorAdapter(
    private val contexto: Context,
    private val listaSupervisores: MutableList<Supervisores>,
    private val onEliminarSupervisor: (Int, Int) -> Unit // posición, idSupervisor
) : RecyclerView.Adapter<SupervisorAdapter.SupervisorViewHolder>() {

    class SupervisorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId       = itemView.findViewById<TextView>(R.id.tvItemCodigo)
        val tvNombre   = itemView.findViewById<TextView>(R.id.tvItemNombre)
        val tvApellido = itemView.findViewById<TextView>(R.id.tvItemApellido)
        val tvDni      = itemView.findViewById<TextView>(R.id.tvItemDni)
        val tvSueldo   = itemView.findViewById<TextView>(R.id.tvItemSueldo)
        val tvTurno    = itemView.findViewById<TextView>(R.id.tvItemTurno)
        val fotoName   = itemView.findViewById<TextView>(R.id.tvNameImage)
        val imgFoto    = itemView.findViewById<ImageView>(R.id.imgFoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupervisorViewHolder {
        val vistaSupervisor = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_supervisor, parent, false)
        return SupervisorViewHolder(vistaSupervisor)
    }

    override fun onBindViewHolder(holder: SupervisorViewHolder, posicion: Int) {
        val supervisor = listaSupervisores[posicion]

        holder.tvId.text       = supervisor.idE.toString()
        holder.tvNombre.text   = supervisor.nombreE
        holder.tvApellido.text = supervisor.apellidoE
        holder.tvDni.text      = supervisor.dniE
        holder.tvSueldo.text   = supervisor.sueldoE.toString()
        holder.tvTurno.text    = supervisor.turnoE
        holder.fotoName.text   = supervisor.imagenE

        // Cargar imagen desde almacenamiento externo
        val storageDir = contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val archivoImagen = File(storageDir, supervisor.imagenE)

        if (archivoImagen.exists()) {
            val bitmap = BitmapFactory.decodeFile(archivoImagen.absolutePath)
            holder.imgFoto.setImageBitmap(bitmap)
        } else {
            holder.imgFoto.setImageResource(R.drawable.imagen_default)
        }

        // Al hacer clic, abre la pantalla para editar el supervisor
        holder.itemView.setOnClickListener {
            val intent = Intent(contexto, detalle_supervisor::class.java)
            intent.putExtra("modoEditar", true)
            intent.putExtra("idSupervisor", supervisor.idE)
            contexto.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listaSupervisores.size

    //! Este método elimina visualmente un supervisor de la lista y actualiza el RecyclerView
    fun eliminarDeLista(posicion: Int) {
        listaSupervisores.removeAt(posicion)
        notifyItemRemoved(posicion)
    }

    // Comentado porque no se está utilizando actualmente. Lo puedes borrar si quieres.
    /*
    fun eliminarItem(posicion: Int) {
        val id = listaSupervisores[posicion].idE
        onEliminarSupervisor(posicion, id)
    }
    */
}