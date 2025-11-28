import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ListarClientesComponent } from './listar-clientes.component';
import { ClienteService } from '../../../../core/services/cliente.service';
import { ErrorService } from '../../../../core/services/error.service';
import { RouterTestingModule } from '@angular/router/testing';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';

describe('ListarClientesComponent (NO standalone)', () => {
  let component: ListarClientesComponent;
  let fixture: ComponentFixture<ListarClientesComponent>;

  let mockService: jest.Mocked<ClienteService>;
  let mockErrorService: jest.Mocked<ErrorService>;

  beforeEach(async () => {
    mockService = { listar: jest.fn() } as any;
    mockErrorService = { parse: jest.fn().mockReturnValue('Error') } as any;

    await TestBed.configureTestingModule({
      declarations: [ListarClientesComponent],   // ⬅️ NO standalone
      imports: [
        CommonModule,                            // ⬅️ Necesario siempre
        FormsModule,                             // ⬅️ Por ngModel
        RouterTestingModule                      // ⬅️ Reemplaza Router
      ],
      providers: [
        { provide: ClienteService, useValue: mockService },
        { provide: ErrorService, useValue: mockErrorService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListarClientesComponent);
    component = fixture.componentInstance;
  });

  it('Debe cargar y listar clientes correctamente', () => {
    const clientesMock = [
      { nombre: 'Manuel', genero: 'M', edad: 30, identificacion: '1001', direccion: 'Ibarra', telefono: '09' },
      { nombre: 'Ana', genero: 'F', edad: 28, identificacion: '1002', direccion: 'Ibarra', telefono: '08' }
    ];

    mockService.listar.mockReturnValue(of(clientesMock));

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.clientes.length).toBe(2);
    expect(component.filtered.length).toBe(2);
  });

  it('Debe manejar error del servicio', () => {
    mockService.listar.mockReturnValue(
      throwError(() => new Error('Error de servicio'))
    );

    component.ngOnInit();
    fixture.detectChanges();

    expect(component.clientes.length).toBe(0);
    expect(component.errorMessage).toBe('Error'); // viene del mockErrorService
  });
});
