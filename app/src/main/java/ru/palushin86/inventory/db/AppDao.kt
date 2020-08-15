package ru.palushin86.inventory.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.palushin86.inventory.db.entities.InventoryEntityDb
import ru.palushin86.inventory.db.entities.ParameterEntityDb
import ru.palushin86.inventory.db.entities.ParameterTypeEntityDb
import ru.palushin86.inventory.entities.Inventory
import ru.palushin86.inventory.entities.Parameter
import ru.palushin86.inventory.entities.ParameterType

@Dao
interface AppDao {

    @Query("SELECT * from parametertypeentitydb")
    fun getParameterTypes(): List<ParameterTypeEntityDb>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(parameterType: ParameterTypeEntityDb): Long

    @Query("DELETE FROM parametertypeentitydb WHERE id=:id")
    fun deleteParameterType(id: Int)


    @Query("SELECT * from inventoryentitydb")
    fun getEquipments(): List<InventoryEntityDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parameterType: InventoryEntityDb): Long


    @Query("SELECT * from parameterentitydb WHERE inventoryId=:inventoryId")
    fun getParameters(inventoryId: Int): List<ParameterEntityDb>

    @Query("SELECT * from parameterentitydb")
    fun getParameters(): List<ParameterEntityDb>

    @Query("DELETE FROM inventoryentitydb WHERE id=:id")
    fun removeInventory(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parameters: List<ParameterEntityDb>)

    @Query("SELECT * from inventoryentitydb WHERE id=:inventoryId")
    fun getEquipment(inventoryId: Int): InventoryEntityDb

    /*@Query("SELECT * from  ORDER BY name ASC")
    fun getEquipemtTypes(): LiveData<List<EquipmentType>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(equipmentType: EquipmentType)

    @Delete
    fun delete(equipmentType: EquipmentType)



    @Query("SELECT * from equipment ORDER BY name ASC")
    fun getEquipments(): LiveData<List<Equipment>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(equipment: Equipment)

    @Delete
    fun delete(equipment: Equipment)

    @Query("SELECT * from equipment WHERE cabinetId==:cabinetId")
    fun getEquipmentsByCabinetId(cabinetId: Int?): LiveData<List<Equipment>>

    @Query("SELECT * from equipment WHERE equipmentTypeId==:equipmentTypeId")
    fun getEquipmentsByEquipmentTypeId(equipmentTypeId: Int?): LiveData<List<Equipment>>*/
/*
    @Query("SELECT * from cabinet ORDER BY name ASC")
    fun getCabinets(): LiveData<List<Cabinet>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cabinet: Cabinet)

    @Delete
    fun delete(cabinet: Cabinet)*/

}