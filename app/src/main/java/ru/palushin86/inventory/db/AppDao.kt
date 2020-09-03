package ru.palushin86.inventory.db

import androidx.room.*
import ru.palushin86.inventory.db.entities.*
import ru.palushin86.inventory.entities.FilterSet
import ru.palushin86.inventory.entities.Parameter
import ru.palushin86.inventory.entities.Tag


@Dao
interface AppDao {

    @Query("SELECT * from tagentitydb")
    fun getTags(): List<TagEntityDb>

    @Query("SELECT * from tagentitydb WHERE id=:tagId")
    fun getTag(tagId: Int): Tag

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tag: TagEntityDb): Long

    @Query("DELETE FROM tagentitydb WHERE id=:id")
    fun deleteTag(id: Int)

    @Query("UPDATE tagentitydb SET isAutocomplete = :isAutocomplete WHERE id = :id")
    fun updateTag(id: Int, isAutocomplete: Boolean)

    @Update
    fun update(tagEntityDb: TagEntityDb)


    @Query("SELECT * from inventoryentitydb")
    fun getEquipments(): List<InventoryEntityDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parameterType: InventoryEntityDb): Long


    @Query("SELECT * from parameterentitydb WHERE inventoryId=:inventoryId")
    fun getParameters(inventoryId: Int): List<ParameterEntityDb>

    @Query("SELECT * from parameterentitydb")
    fun getParameters(): List<ParameterEntityDb>

    @Query("SELECT * from parameterentitydb WHERE tagId=:tagId")
    fun getParametersByTag(tagId: Int): List<ParameterEntityDb>

    @Query("DELETE FROM inventoryentitydb WHERE id=:id")
    fun removeInventory(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parameters: List<ParameterEntityDb>)

    @Query("SELECT * from inventoryentitydb WHERE id=:inventoryId")
    fun getEquipment(inventoryId: Int): InventoryEntityDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filterSet: FilterSetEntityDb): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilters(filterSet: List<FilterEntityDb>)

    @Query("SELECT * from filtersetentitydb")
    fun getFilterSets(): List<FilterSetEntityDb>

    @Query("SELECT * from filterentitydb WHERE id=:id")
    fun getFilters(id: Int): List<FilterEntityDb>

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