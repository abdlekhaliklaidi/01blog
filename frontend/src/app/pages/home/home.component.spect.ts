// import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
// import { LoginComponent } from './login/login.component';
// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
// import { RouterTestingModule } from '@angular/router/testing';
// import { FormsModule } from '@angular/forms';
// import { Router } from '@angular/router';

// describe('LoginComponent', () => {
//   let component: LoginComponent;
//   let fixture: ComponentFixture<LoginComponent>;
//   let httpMock: HttpTestingController;
//   let router: Router;

//   beforeEach(async () => {
//     await TestBed.configureTestingModule({
//       imports: [
//         HttpClientTestingModule,
//         RouterTestingModule.withRoutes([]),
//         FormsModule
//       ],
//       declarations: [LoginComponent]
//     }).compileComponents();

//     fixture = TestBed.createComponent(LoginComponent);
//     component = fixture.componentInstance;
//     httpMock = TestBed.inject(HttpTestingController);
//     router = TestBed.inject(Router);
//     fixture.detectChanges();
//   });

//   afterEach(() => {
//     httpMock.verify();
//   });

//   it('should create', () => {
//     expect(component).toBeTruthy();
//   });

//   it('should show error if email or password is empty', () => {
//     component.credentials.email = '';
//     component.credentials.password = '';
//     component.onLogin();
//     expect(component.errorMessage).toBe('Veuillez entrer un email et un mot de passe');
//     expect(component.successMessage).toBe('');
//   });

//   it('should call API and navigate on successful login', fakeAsync(() => {
//     spyOn(router, 'navigate');

//     component.credentials.email = 'test@example.com';
//     component.credentials.password = 'password123';

//     component.onLogin();

//     const req = httpMock.expectOne('http://localhost:8080/users/login');
//     expect(req.request.method).toBe('POST');
//     expect(req.request.body).toEqual({
//       email: 'test@example.com',
//       password: 'password123'
//     });

//     req.flush('Connexion réussie');

//     tick();

//     expect(component.successMessage).toBe('Connexion réussie');
//     expect(component.errorMessage).toBe('');
//     expect(router.navigate).toHaveBeenCalledWith(['/home']);
//   }));

//   it('should show error message on failed login', fakeAsync(() => {
//     component.credentials.email = 'test@example.com';
//     component.credentials.password = 'wrongpassword';

//     component.onLogin();

//     const req = httpMock.expectOne('http://localhost:8080/users/login');
//     expect(req.request.method).toBe('POST');

//     req.flush('Erreur de connexion', { status: 401, statusText: 'Unauthorized' });

//     tick();

//     expect(component.errorMessage).toBe('Erreur de connexion');
//     expect(component.successMessage).toBe('');
//   }));
// });
