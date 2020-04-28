package me.ostrovski.fib.repository

import me.ostrovski.fib.model.MatrixRecord
import org.springframework.data.repository.CrudRepository

/**
 * Interface for accessing power matrixes from the storage (i.e., Redis).
 */
interface PowersRepository : CrudRepository<MatrixRecord, Int>
