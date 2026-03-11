package micro.gym.equipmentmanagementservice.controller;

import micro.gym.equipmentmanagementservice.model.*;
import micro.gym.equipmentmanagementservice.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@Tag(name = "Equipment", description = "Gestión de equipos del gimnasio")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    @Operation(summary = "Obtener todos los equipos", description = "Obtiene el listado completo de todos los equipos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipos obtenidos exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipment.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos")
    })
    public List<Equipment> getAll() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    @Operation(summary = "Obtener equipo por ID", description = "Obtiene los detalles de un equipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipo obtenido exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipment.class))),
        @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos")
    })
    public Equipment getById(
        @Parameter(description = "ID del equipo", required = true)
        @PathVariable String id) {
        return equipmentService.getEquipment(new EquipmentId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo equipo", description = "Registra un nuevo equipo en el gimnasio. Solo ADMIN puede crear")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Equipo creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipment.class),
                examples = @ExampleObject(value = "{ \"id\": \"EQ001\", \"name\": \"Caminadora\", \"type\": \"CARDIO\", \"quantity\": { \"value\": 5 }, \"status\": \"AVAILABLE\" }"))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos")
    })
    public Equipment create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del equipo a crear",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipment.class),
                examples = @ExampleObject(value = "{ \"name\": \"Caminadora\", \"type\": \"CARDIO\", \"quantity\": { \"value\": 5 } }")))
        @RequestBody Equipment equipment) {
        return equipmentService.createEquipment(equipment);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambiar estado del equipo", description = "Actualiza el estado de un equipo (disponible, mantenimiento, etc.). Solo ADMIN puede cambiar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos")
    })
    public void changeStatus(
        @Parameter(description = "ID del equipo", required = true)
        @PathVariable String id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nuevo estado del equipo (AVAILABLE, MAINTENANCE, BROKEN, ARCHIVED)",
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "\"MAINTENANCE\"")))
        @RequestBody EquipmentStatus newStatus) {
        equipmentService.changeStatus(new EquipmentId(id), newStatus);
    }

    @PutMapping("/{id}/quantity")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar cantidad de equipo", description = "Modifica la cantidad disponible de un equipo. Solo ADMIN puede actualizar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Cantidad inválida"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos")
    })
    public void updateQuantity(
        @Parameter(description = "ID del equipo", required = true)
        @PathVariable String id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Nueva cantidad del equipo",
            required = true,
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "10")))
        @RequestBody int quantity) {
        equipmentService.updateQuantity(new EquipmentId(id), quantity);
    }
}
