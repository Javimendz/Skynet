package com.example.skynet.ui.main;

import android.app.Dialog;
import com.example.skynet.Config;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.skynet.R;
import com.example.skynet.data.remote.ApiService;
import com.example.skynet.data.remote.RetrofitClient;
import com.example.skynet.data.remote.dto.ApiResponseDto;
import com.example.skynet.data.remote.dto.ExerciseApiRequestDto;
import com.example.skynet.data.remote.dto.FatSecretRecipeDto;
import com.example.skynet.data.remote.dto.FatSecretResponseDto;
import com.example.skynet.data.remote.dto.NutricionResponseDto;
import com.example.skynet.data.remote.dto.PerfilResponseDto;
import com.example.skynet.data.remote.dto.ReservaResponseDto;
import com.example.skynet.data.remote.dto.RutinaResponseDto;
import com.example.skynet.data.remote.dto.TutorialResponseDto;
import com.example.skynet.data.remote.dto.HorarioResponseDto;
import com.example.skynet.ui.ejercicios.ReproductorFragment;
import com.google.gson.Gson;
import com.example.skynet.ui.auth.LoginActivity;
import com.example.skynet.ui.clases.ClasesFragment;
import com.example.skynet.ui.rutinas.RutinasFragment;
import com.example.skynet.ui.perfil.PerfilFragment;
import com.example.skynet.ui.contacto.ContactoFragment;
import com.example.skynet.ui.reservas.ReservasFragment;
import com.example.skynet.ui.equipo.EquipoFragment;
import com.example.skynet.ui.servicios.SuplementacionFragment;
import com.example.skynet.ui.dieta.DietaFragment;
import com.example.skynet.ui.entrenamiento.EntrenamientoFragment;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.example.skynet.ui.social.SocialFragment;
import com.example.skynet.ui.notificaciones.NotificacionesFragment;
import com.example.skynet.ui.salud.SaludFragment;
import com.example.skynet.ui.staff.StaffFragment;
import com.example.skynet.data.remote.dto.EvolucionPesoDto;
import com.example.skynet.data.remote.dto.LogroResponseDto;
import com.example.skynet.data.remote.dto.VisualizacionResponseDto;
import com.example.skynet.ui.ejercicios.TutorialViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.Collections;
import com.example.skynet.ui.view.EspacioFragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import android.content.res.ColorStateList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import kotlin.Unit;

public class DesarrolloActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private DrawerLayout drawer;
    private View homeContent;
    private BottomNavigationView bottomNav;
    private ImageView profileImage;
    private ImageView ivHeaderPhoto;
    
    // Widget Nutrición
    private View cardNutricionWidget;
    private TextView tvNombrePlanWidget, tvCaloriasWidget;
    private TextView tvProtLabel, tvCarbLabel, tvGrasLabel;
    private TextView tvIANewsTitle, tvIANewsContent;
    private com.google.android.material.chip.ChipGroup chipGroupCategories;
    private TextView tvStreakCount, tvAforoPorcentaje;
    private View statusAforoIndicator;
    private com.google.android.material.card.MaterialCardView cardAforoWidget, cardResumeWorkout, cardQuickLogNutricion;
    private TextView tvLastExerciseName;
    private com.google.android.material.progressindicator.CircularProgressIndicator progressCircularStat;
    private com.google.android.material.progressindicator.LinearProgressIndicator progressProt, progressCarb, progressGras, progressReservas;
    private View wsStatusIndicator;
    private View dotL, dotM, dotMi, dotJ, dotV, dotS, dotD;
    private View layoutBroadcast;
    private TextView tvActividadPorcentaje, tvReservasValor, tvBroadcastTitle, tvBroadcastContent;
    private androidx.compose.ui.platform.ComposeView composeWeeklyPerformance, composeCategorySelector;
    private List<Boolean> diasCompletadosGlobal = new ArrayList<>(Collections.nCopies(7, false));
    private int actividadPorcentajeGlobal = 0;
    private int reservasCountGlobal = 0;
    private FriendAdapter friendAdapter;
    private LineChart weightLineChart;
    private SlidingRootNav slidingRootNav;
    private androidx.recyclerview.widget.RecyclerView rvReservasRapidas;
    private androidx.recyclerview.widget.RecyclerView rvAchievements;
    private TextView tvEmptyAchievements;
    private TextView tvWeightChartError;
    private androidx.recyclerview.widget.RecyclerView rvFriendsTraining;

    // Carrusel
    private androidx.viewpager2.widget.ViewPager2 viewPagerCarousel, viewPagerExerciseCarousel;
    private CarouselAdapter carouselAdapter, exerciseCarouselAdapter;
    private List<CarouselAdapter.CarouselItem> carouselItemsList = new ArrayList<>();
    private List<CarouselAdapter.CarouselItem> exerciseCarouselItemsList = new ArrayList<>();

    private com.example.skynet.data.remote.StompManager stompManager;
    private LottieAnimationView notificationsAnim;
    private View notificationBadge;
    private List<com.example.skynet.data.remote.dto.NotificacionResponseDto> notificacionesSesion = new ArrayList<>();
    private BookingAdapter bookingAdapter;

    private TutorialViewModel tutorialViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desarrollo_prueba);

        tutorialViewModel = new ViewModelProvider(this).get(TutorialViewModel.class);

        notificationBadge = findViewById(R.id.notificationBadge);

        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);

        // 1. Configurar Fecha Actual
        TextView tvFecha = findViewById(R.id.tvSaludoLabel);
        if (tvFecha != null) {
            Date calendario = Calendar.getInstance().getTime();
            SimpleDateFormat formato = new SimpleDateFormat("EEEE, d MMMM", new Locale("es", "ES"));
            String fechaFormateada = formato.format(calendario);
            tvFecha.setText(fechaFormateada.substring(0, 1).toUpperCase() + fechaFormateada.substring(1));
        }

        // 2. Configurar Saludo e Imagen con SharedPreferences
        profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> cargarFragmento(new PerfilFragment()));

        TextView tvSaludo = findViewById(R.id.tvSaludo);
        if (tvSaludo != null) {
            String nombreUsuario = prefs.getString("nombre_usuario", "Usuario GymCrush");
            tvSaludo.setText("¡A tope, " + nombreUsuario.replace("GymCrush", "").trim() + "!");
        }
        
        // Widget Nutrición initialization
        cardNutricionWidget = findViewById(R.id.cardNutricionWidget);
        tvNombrePlanWidget = findViewById(R.id.tvNombrePlanWidget);
        tvCaloriasWidget = findViewById(R.id.tvCaloriasWidget);
        tvIANewsTitle = findViewById(R.id.tvIANewsTitle);
        tvIANewsContent = findViewById(R.id.tvIANewsContent);
        
        tvActividadPorcentaje = findViewById(R.id.tvActividadPorcentaje);
        tvReservasValor = findViewById(R.id.tvReservasValor);
        progressCircularStat = findViewById(R.id.progressCircularStat);
        progressReservas = findViewById(R.id.progressReservas);

        tvStreakCount = findViewById(R.id.tvStreakCount);
        tvAforoPorcentaje = findViewById(R.id.tvAforoPorcentaje);
        statusAforoIndicator = findViewById(R.id.statusAforoIndicator);
        cardAforoWidget = findViewById(R.id.cardAforoWidget);

        // Inicialización de nuevos widgets de utilidad (Smart Actions)
        cardResumeWorkout = findViewById(R.id.cardResumeWorkout);
        tvLastExerciseName = findViewById(R.id.tvLastExerciseName);
        cardQuickLogNutricion = findViewById(R.id.cardQuickLogNutricion);

        if (cardResumeWorkout != null) {
            cardResumeWorkout.setOnClickListener(v -> {
                // Lógica para saltar directamente a la ejecución del último ejercicio guardado
                Toast.makeText(this, "Retomando sesión: Press Banca...", Toast.LENGTH_SHORT).show();
            });
        }

        if (cardQuickLogNutricion != null) {
            cardQuickLogNutricion.setOnClickListener(v -> {
                // Navega al fragmento de dieta con el buscador pre-enfocado
                DietaFragment fragment = new DietaFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("focusSearch", true);
                fragment.setArguments(bundle);
                cargarFragmento(fragment);
            });
        }

        dotL = findViewById(R.id.dotL);
        dotM = findViewById(R.id.dotM);
        dotMi = findViewById(R.id.dotMi);
        dotJ = findViewById(R.id.dotJ);
        dotV = findViewById(R.id.dotV);
        dotS = findViewById(R.id.dotS);
        dotD = findViewById(R.id.dotD);

        layoutBroadcast = findViewById(R.id.layoutBroadcast);
        tvBroadcastTitle = findViewById(R.id.tvBroadcastTitle);
        tvBroadcastContent = findViewById(R.id.tvBroadcastContent);
        View btnCerrarBroadcast = findViewById(R.id.btnCerrarBroadcast);
        if (btnCerrarBroadcast != null) {
            btnCerrarBroadcast.setOnClickListener(v -> layoutBroadcast.setVisibility(View.GONE));
        }

        rvReservasRapidas = findViewById(R.id.rvReservasRapidas);
        rvAchievements = findViewById(R.id.rvAchievements);
        tvEmptyAchievements = findViewById(R.id.tvEmptyAchievements);
        tvWeightChartError = findViewById(R.id.tvWeightChartError);
        rvFriendsTraining = findViewById(R.id.rvFriendsTraining);

        composeWeeklyPerformance = findViewById(R.id.composeWeeklyPerformance);
        composeCategorySelector = findViewById(R.id.composeCategorySelector);
        setupWeeklyPerformanceCompose();
        setupCategorySelectorCompose();
        friendAdapter = new FriendAdapter();
        if (rvFriendsTraining != null) {
            rvFriendsTraining.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
            rvFriendsTraining.setAdapter(friendAdapter);
        }

        weightLineChart = findViewById(R.id.weightLineChart);
        View cardWeightChart = findViewById(R.id.cardWeightChart);
        if (cardWeightChart != null) {
            cardWeightChart.setOnClickListener(v -> {
                Intent intent = new Intent(DesarrolloActivity.this, com.example.skynet.ui.salud.RegistroSaludActivity.class);
                startActivityForResult(intent, 102);
            });
        }
        updateWeightChart();

        // chipGroupCategories = findViewById(R.id.chipGroupCategories);
        // setupCategoryChips();

        progressProt = findViewById(R.id.progressProt);
        progressCarb = findViewById(R.id.progressCarb);
        progressGras = findViewById(R.id.progressGras);

        tvProtLabel = findViewById(R.id.tvProtLabel);
        tvCarbLabel = findViewById(R.id.tvCarbLabel);
        tvGrasLabel = findViewById(R.id.tvGrasLabel);
        
        cardNutricionWidget.setOnClickListener(v -> {
            DietaFragment fragment = new DietaFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("mostrarHistorial", true);
            fragment.setArguments(bundle);
            cargarFragmento(fragment);
        });

        // 3. Configurar Menú Lateral Moderno con SlidingRootNav
        ImageView btnMenu = findViewById(R.id.btnMenuLateral);
        
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.menu_left_drawer) // Crearemos este layout
                .withMenuLocked(false)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .inject();

        btnMenu.setOnClickListener(v -> {
            if (slidingRootNav.isMenuOpened()) {
                slidingRootNav.closeMenu();
            } else {
                slidingRootNav.openMenu();
            }
        });

        // Configurar clics y datos en el nuevo menú
        setupModernMenu();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        // Ocultamos el NavigationView viejo pero lo dejamos por si acaso o lo borramos después
        navigationView.setVisibility(View.GONE);
        
        drawer = findViewById(R.id.drawer_layout_root);
        
        cargarDatosYFotoPerfil();

        // Configurar roles y listener del menú
        Set<String> roles = prefs.getStringSet("roles", new HashSet<>());
        boolean isAdmin = roles.contains("ROLE_ADMIN") || roles.contains("ADMIN");
        
        if (isAdmin) {
            navigationView.getMenu().findItem(R.id.nav_staff).setVisible(true);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_salir) {
                mostrarDialogoCerrarSesion();
            } else if (id == R.id.nav_qr) {
                mostrarDialogoQR();
            } else if (id == R.id.nav_espacio) {
                cargarFragmento(new EspacioFragment());
            } else if (id == R.id.nav_reservas) {
                cargarFragmento(new ReservasFragment());
            } else if (id == R.id.nav_salud) {
                cargarFragmento(new SaludFragment());
            } else if (id == R.id.nav_equipo) {
                cargarFragmento(new EquipoFragment());
            } else if (id == R.id.nav_contacto) {
                cargarFragmento(new ContactoFragment());
            } else if (id == R.id.nav_sumplementacion) {
                cargarFragmento(new SuplementacionFragment());
            } else if (id == R.id.nav_historial_dieta) {
                cargarFragmento(new DietaFragment());
            } else if (id == R.id.nav_clases) {
                cargarFragmento(new ClasesFragment());
            } else if (id == R.id.nav_staff) {
                cargarFragmento(new StaffFragment());
            }
            if (drawer != null) {
                drawer.closeDrawer(GravityCompat.START);
            }
            return true;
        });

        // 4. Configurar Bottom Navigation
        homeContent = findViewById(R.id.home_content);
        bottomNav = findViewById(R.id.botton_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                mostrarHome();
                return true;
            } else if (id == R.id.nav_entrenamiento) {
                cargarFragmento(new EntrenamientoFragment());
                return true;
            } else if (id == R.id.nav_social) {
                cargarFragmento(new SocialFragment());
                return true;
            } else if (id == R.id.nav_perfil) {
                cargarFragmento(new PerfilFragment());
                return true;
            }
            return false;
        });

        // 5. Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        notificationsAnim = findViewById(R.id.btnNotificationsAnim);
        notificationsAnim.setOnClickListener(v -> {
            notificationsAnim.playAnimation();
            cargarFragmento(new NotificacionesFragment());
        });

        notificationsAnim.setOnLongClickListener(v -> {
            simulateActiveFriendsUpdate();
            return true;
        });

        // 6. Iniciar WebSockets para Notificaciones y Aforo
        iniciarWebSockets(usuarioId);

        // Configurar el carrusel
        setupCarousel();
        setupExerciseCarousel();
        setupBookingCarousel();
        setupAchievements();
        cargarNoticiasIA();
        actualizarRendimientoSemanal();
        calcularRachaActual(usuarioId);

        // Listener único para manejar la visibilidad del Home y refrescar datos al volver de fragmentos
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                homeContent.setVisibility(View.VISIBLE);
                findViewById(R.id.fragment_container).setVisibility(View.GONE);
                bottomNav.setSelectedItemId(R.id.nav_home);
                drawer = findViewById(R.id.drawer_layout_root);
        
        cargarDatosYFotoPerfil();
                actualizarRendimientoSemanal();
                calcularRachaActual(usuarioId);
            }
        });
    }

    private void setupCategorySelectorCompose() {
        if (composeCategorySelector == null) return;
        
        List<String> categories = java.util.Arrays.asList("Todos", "Fuerza", "Cardio", "Yoga", "Hipertrofia", "Resistencia");
        
        WeeklyPerformanceComposeKt.setupGlassCategorySelector(
            composeCategorySelector,
            categories,
            "Todos",
            category -> {
                String catCode = category.toUpperCase();
                if (catCode.equals("TODOS")) catCode = "ALL";
                filtrarCarruselPorCategoria(catCode);
                return kotlin.Unit.INSTANCE;
            }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            updateWeightChart();
        }
    }

    private void cargarNoticiasIA() {
        // En una app real, aquí llamarías a un endpoint que use GPT o similar.
        // Simulamos la respuesta de la IA basada en el contexto del usuario.
        new android.os.Handler().postDelayed(() -> {
            if (isFinishing()) return;

            String[] titulos = {
                    "Optimización de Hipertrofia",
                    "Tendencias en Biohacking",
                    "Recuperación Inteligente",
                    "IA en el Levantamiento"
            };
            
            String[] noticias = {
                    "Nuevos estudios sugieren que la fase excéntrica lenta (3s) incrementa la síntesis proteica un 15% más que el ritmo convencional.",
                    "El uso de luz roja tras entrenamientos de alta intensidad podría reducir el DOMS en un 20%. ¡Pruébalo esta semana!",
                    "Tu ritmo de entrenamiento indica que hoy es un día óptimo para series de aproximación pesadas. Enfócate en la técnica del peso muerto.",
                    "La inteligencia artificial predice que tu mejor rendimiento hoy será en la ventana de las 18:00. Hidrátate bien."
            };

            int random = new java.util.Random().nextInt(titulos.length);
            tvIANewsTitle.setText(titulos[random]);
            tvIANewsContent.setText(noticias[random]);
            
            // Animación suave de aparición
            findViewById(R.id.cardIANews).setAlpha(0);
            findViewById(R.id.cardIANews).animate().alpha(1).setDuration(800).start();

        }, 2000);
    }

    private void setupBookingCarousel() {
        rvReservasRapidas = findViewById(R.id.rvReservasRapidas);
        bookingAdapter = new BookingAdapter(horario -> {
            SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
            long usuarioId = prefs.getLong("user_id", -1);
            if (usuarioId != -1) {
                RetrofitClient.getApiService().crearReserva(usuarioId, horario.getId()).enqueue(new Callback<ApiResponseDto<ReservaResponseDto>>() {
                    @Override
                    public void onResponse(Call<ApiResponseDto<ReservaResponseDto>> call, Response<ApiResponseDto<ReservaResponseDto>> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DesarrolloActivity.this, "¡Reserva realizada con éxito!", Toast.LENGTH_SHORT).show();
                            actualizarRendimientoSemanal();
                        } else {
                            Toast.makeText(DesarrolloActivity.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponseDto<ReservaResponseDto>> call, Throwable t) {
                        Toast.makeText(DesarrolloActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        rvReservasRapidas.setAdapter(bookingAdapter);

        // Cargar horarios disponibles
        RetrofitClient.getApiService().getTodosLosHorarios().enqueue(new Callback<ApiResponseDto<List<HorarioResponseDto>>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<List<HorarioResponseDto>>> call, Response<ApiResponseDto<List<HorarioResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bookingAdapter.updateItems(response.body().getDatos());
                }
            }
            @Override public void onFailure(Call<ApiResponseDto<List<HorarioResponseDto>>> call, Throwable t) {}
        });
    }

    private void setupWeeklyPerformanceCompose() {
        if (composeWeeklyPerformance == null) return;
        composeWeeklyPerformance.setViewCompositionStrategy(
            androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed.INSTANCE
        );
        updateWeeklyPerformanceComposeContent();
    }

    private void updateWeeklyPerformanceComposeContent() {
        if (composeWeeklyPerformance == null) return;
        
        WeeklyPerformanceData data = new WeeklyPerformanceData(
            (float) actividadPorcentajeGlobal,
            reservasCountGlobal,
            5,
            diasCompletadosGlobal
        );
        
        WeeklyPerformanceComposeKt.setupWeeklyPerformanceCompose(composeWeeklyPerformance, data);
    }

    private void setupAchievements() {
        if (rvAchievements == null) return;
        rvAchievements.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        
        RetrofitClient.getApiService().obtenerLogros().enqueue(new Callback<ApiResponseDto<List<LogroResponseDto>>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<List<LogroResponseDto>>> call, Response<ApiResponseDto<List<LogroResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                    List<LogroResponseDto> logros = response.body().getDatos();
                    if (logros.isEmpty()) {
                        runOnUiThread(() -> {
                            if (tvEmptyAchievements != null) tvEmptyAchievements.setVisibility(View.VISIBLE);
                            rvAchievements.setVisibility(View.GONE);
                        });
                        return;
                    }
                    runOnUiThread(() -> {
                        if (tvEmptyAchievements != null) tvEmptyAchievements.setVisibility(View.GONE);
                        rvAchievements.setVisibility(View.VISIBLE);
                    });
                    List<Achievement> achievements = new ArrayList<>();
                    for (LogroResponseDto l : logros) {
                        String fecha = l.getFechaDesbloqueo() != null ? l.getFechaDesbloqueo() : 
                                     (l.getPorcentajeProgreso() != null ? l.getPorcentajeProgreso().intValue() + "%" : "En progreso");
                        achievements.add(new Achievement(l.getNombre(), l.getIcono(), fecha));
                    }
                    AchievementAdapter adapter = new AchievementAdapter(achievements);
                    rvAchievements.setAdapter(adapter);
                } else {
                    Log.e("API_LOGROS", "Respuesta no exitosa al cargar logros: " + response.code());
                    runOnUiThread(() -> {
                        if (tvEmptyAchievements != null) {
                            tvEmptyAchievements.setText("Error al cargar logros. Revisa tu conexión. 🔄");
                            tvEmptyAchievements.setVisibility(View.VISIBLE);
                        }
                        rvAchievements.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDto<List<LogroResponseDto>>> call, Throwable t) {
                Log.e("API_LOGROS", "Error al cargar logros", t);
                runOnUiThread(() -> {
                    if (tvEmptyAchievements != null) {
                        tvEmptyAchievements.setText("Error de red al cargar logros. Inténtalo de nuevo. 🔄");
                        tvEmptyAchievements.setVisibility(View.VISIBLE);
                    }
                    rvAchievements.setVisibility(View.GONE);
                });
            }
        });
    }

    private void setupCategoryChips() {
        chipGroupCategories.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            
            int checkedId = checkedIds.get(0);
            String categoria = "";
            
            if (checkedId == R.id.chipAll) categoria = "ALL";
            else if (checkedId == R.id.chipFuerza) categoria = "FUERZA";
            else if (checkedId == R.id.chipCardio) categoria = "CARDIO";
            else if (checkedId == R.id.chipHipertrofia) categoria = "HIPERTROFIA";
            else if (checkedId == R.id.chipResistencia) categoria = "RESISTENCIA";
            
            filtrarCarruselPorCategoria(categoria);
        });
    }

    private void filtrarCarruselPorCategoria(String categoria) {
        // Lógica para filtrar los carruseles. 
        // Por ahora, simularemos un refresco de datos o filtrado en la lista local.
        if ("ALL".equals(categoria)) {
            cargarDatosDinamicosCarrusel();
            tutorialViewModel.cargarPopulares();
        } else {
            // Aquí llamarías a un endpoint filtrado o filtrarías carouselItemsList / exerciseCarouselItemsList
            // Ejemplo simple:
            Toast.makeText(this, "Filtrando por: " + categoria, Toast.LENGTH_SHORT).show();
            // tutorialViewModel.cargarPorCategoria(categoria); // Si existiera en el ViewModel
        }
    }

    private void setupCarousel() {
        viewPagerCarousel = findViewById(R.id.viewPagerCarousel);
        
        // 1. Inicializar con datos de carga para que no se vea vacío
        carouselItemsList.clear();
        carouselItemsList.add(new CarouselAdapter.CarouselItem(
                "CONSEJO DEL DÍA", 
                "Cargando sugerencias...", 
                "Estamos preparando las mejores recetas para ti", 
                R.drawable.ic_dieta, 
                R.drawable.bg_login_modern, 
                -1
        ));
        
        carouselAdapter = new CarouselAdapter(carouselItemsList);
        viewPagerCarousel.setAdapter(carouselAdapter);

        // 2. Configuración visual de carrusel (Tarjetas laterales visibles)
        viewPagerCarousel.setOffscreenPageLimit(3);
        viewPagerCarousel.setClipToPadding(false);
        viewPagerCarousel.setClipChildren(false);
        
        // Añadir espacio entre tarjetas y efecto de escala
        float nextItemVisiblePx = getResources().getDimension(R.dimen.viewpager_next_item_visible);
        float currentItemHorizontalMarginPx = getResources().getDimension(R.dimen.viewpager_current_item_horizontal_margin);
        float pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx;
        
        androidx.viewpager2.widget.CompositePageTransformer compositePageTransformer = new androidx.viewpager2.widget.CompositePageTransformer();
        compositePageTransformer.addTransformer(new androidx.viewpager2.widget.MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.95f + r * 0.05f);
        });
        viewPagerCarousel.setPageTransformer(compositePageTransformer);

        cargarDatosDinamicosCarrusel();
    }

    private void setupExerciseCarousel() {
        viewPagerExerciseCarousel = findViewById(R.id.viewPagerExerciseCarousel);
        
        exerciseCarouselItemsList.add(new CarouselAdapter.CarouselItem(
                "CONTINUAR VIENDO", 
                "Tus pendientes...", 
                "Cargando tus progresos...", 
                R.drawable.ic_workout,
                R.drawable.bg_login_modern, 
                -1
        ));
        
        exerciseCarouselAdapter = new CarouselAdapter(exerciseCarouselItemsList);
        exerciseCarouselAdapter.setOnItemClickListener(item -> {
            initReproductor(item, "", null, "");
        });
        viewPagerExerciseCarousel.setAdapter(exerciseCarouselAdapter);

        // ... resto de la configuración del ViewPager ...
        // Configuración para mostrar múltiples tarjetas (que se asomen las de los lados)
        viewPagerExerciseCarousel.setOffscreenPageLimit(3);
        viewPagerExerciseCarousel.setClipToPadding(false);
        viewPagerExerciseCarousel.setClipChildren(false);
        // Añadimos padding horizontal para que se vean las tarjetas laterales
        int padding = (int) (40 * getResources().getDisplayMetrics().density);
        viewPagerExerciseCarousel.setPadding(padding, 0, padding, 0);

        androidx.viewpager2.widget.CompositePageTransformer compositePageTransformer = new androidx.viewpager2.widget.CompositePageTransformer();
        compositePageTransformer.addTransformer(new androidx.viewpager2.widget.MarginPageTransformer((int) (10 * getResources().getDisplayMetrics().density)));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f); // Reducimos un poco más las laterales
        });
        viewPagerExerciseCarousel.setPageTransformer(compositePageTransformer);

        observarPendientes();
        observarPopulares();
        
        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);
        if (usuarioId != -1) {
            tutorialViewModel.cargarPendientes(usuarioId);
        }
        tutorialViewModel.cargarPopulares();
    }

    private void observarPopulares() {
        tutorialViewModel.getPopulares().observe(this, populares -> {
            if (populares != null && !populares.isEmpty()) {
                // Si no hay pendientes, o para complementar, añadimos los populares al inicio
                // Para no duplicar, podríamos filtrar o simplemente limpiar y re-añadir
                // Por ahora, vamos a insertarlos si el carrusel está mayormente vacío de contenido "nuestro"
                for (TutorialResponseDto t : populares) {
                    boolean existe = false;
                    for (CarouselAdapter.CarouselItem item : exerciseCarouselItemsList) {
                        if (item.getId() != null && item.getId().equals(t.getId())) {
                            existe = true;
                            break;
                        }
                    }
                    if (!existe) {
                        String repros = t.getContadorReproducciones() != null ? t.getContadorReproducciones() + " visualizaciones" : "Nueva";
                        exerciseCarouselItemsList.add(0, new CarouselAdapter.CarouselItem(
                                "MÁS VISTO",
                                t.getTitulo(),
                                repros,
                                t.getUrlVideo(),
                                null,
                                R.drawable.ic_workout,
                                R.drawable.bg_login_modern,
                                100,
                                t.getId(),
                                t.getDescripcion()
                        ));
                    }
                }
                actualizarAdaptadorEjercicios();
            }
        });
    }

    private void actualizarAdaptadorEjercicios() {
        runOnUiThread(() -> {
            if (exerciseCarouselAdapter != null) {
                exerciseCarouselAdapter.updateItems(new ArrayList<>(exerciseCarouselItemsList));
            }
        });
    }

    private void observarPendientes() {
        tutorialViewModel.getPendientes().observe(this, visualizaciones -> {
            exerciseCarouselItemsList.clear();
            if (visualizaciones != null && !visualizaciones.isEmpty()) {
                for (VisualizacionResponseDto v : visualizaciones) {
                    exerciseCarouselItemsList.add(new CarouselAdapter.CarouselItem(
                            "CONTINUAR VIENDO",
                            v.getTituloTutorial() != null ? v.getTituloTutorial() : "Tutorial",
                            "Progreso: " + (v.getProgresoSegundos() / 60) + " min",
                            v.getUrlVideo(),
                            null,
                            R.drawable.ic_workout,
                            R.drawable.bg_login_modern,
                            v.getProgresoSegundos(),
                            v.getTutorialId(),
                            v.getDescripcionTutorial()
                    ));
                }
            }
            // Cargamos populares después de pendientes
            tutorialViewModel.cargarPopulares();
            // Y finalmente recomendados externos
            cargarEjerciciosRecomendados();
        });
    }

    private void cargarEjerciciosRecomendados() {
        ApiService apiService = RetrofitClient.getApiService();
        apiService.buscarEnBiblioteca("chest").enqueue(new Callback<ApiResponseDto<List<ExerciseApiRequestDto>>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<List<ExerciseApiRequestDto>>> call, Response<ApiResponseDto<List<ExerciseApiRequestDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null && !response.body().getDatos().isEmpty()) {
                    List<ExerciseApiRequestDto> exercises = response.body().getDatos();
                    // No limpiamos, añadimos a lo que ya hay (pendientes)
                    for (int i = 0; i < Math.min(exercises.size(), 8); i++) {
                        ExerciseApiRequestDto ex = exercises.get(i);
                        long id = 0;
                        try {
                            id = Long.parseLong(ex.getId());
                        } catch (Exception e) {
                            id = Math.abs(ex.getId().hashCode());
                        }

                        String detail = "Equipo: " + ex.getEquipment() + " | " + ex.getTarget();
                        if (ex.getInstructions() != null && !ex.getInstructions().isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (String s : ex.getInstructions()) {
                                sb.append(s).append(" ");
                            }
                            detail = sb.toString().trim();
                        }

                        exerciseCarouselItemsList.add(new CarouselAdapter.CarouselItem(
                                ex.getBodyPart().toUpperCase(),
                                ex.getName(),
                                "Equipo: " + ex.getEquipment() + " | " + ex.getTarget(),
                                ex.getGifUrl(),
                                null, // URL de receta no aplica
                                R.drawable.ic_workout,
                                R.drawable.bg_login_modern,
                                100,
                                id,
                                detail
                        ));
                    }
                    runOnUiThread(() -> actualizarAdaptadorEjercicios());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDto<List<ExerciseApiRequestDto>>> call, Throwable t) {
                // Fallback silencioso
            }
        });
    }

    private void cargarDatosDinamicosCarrusel() {
        ApiService apiService = RetrofitClient.getApiService();
        
        // Cargar Recetas de FatSecret para el Carrusel
        apiService.buscarAlimentos("diet").enqueue(new Callback<ApiResponseDto<String>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<String>> call, Response<ApiResponseDto<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                    try {
                        String jsonString = response.body().getDatos();
                        FatSecretResponseDto fatSecretData = new Gson().fromJson(jsonString, FatSecretResponseDto.class);
                        
                        if (fatSecretData != null && fatSecretData.getRecipes() != null && fatSecretData.getRecipes().getRecipe() != null) {
                            carouselItemsList.clear();
                            List<FatSecretRecipeDto> recipes = fatSecretData.getRecipes().getRecipe();
                            
                            // Ordenar: primero las que tienen imagen, luego las que no
                            Collections.sort(recipes, (r1, r2) -> {
                                boolean hasImg1 = r1.getRecipeImage() != null && !r1.getRecipeImage().isEmpty();
                                boolean hasImg2 = r2.getRecipeImage() != null && !r2.getRecipeImage().isEmpty();
                                if (hasImg1 && !hasImg2) return -1;
                                if (!hasImg1 && hasImg2) return 1;
                                return 0;
                            });

                            for (int i = 0; i < Math.min(recipes.size(), 8); i++) {
                                FatSecretRecipeDto recipe = recipes.get(i);
                                String macros = "";
                                if (recipe.getRecipeNutrition() != null) {
                                    macros = recipe.getRecipeNutrition().getCalories() + " kcal | P:" + 
                                             recipe.getRecipeNutrition().getProtein() + "g C:" + 
                                             recipe.getRecipeNutrition().getCarbohydrate() + "g";
                                }

                                carouselItemsList.add(new CarouselAdapter.CarouselItem(
                                        "RECETA SALUDABLE",
                                        recipe.getRecipeName(),
                                        macros.isEmpty() ? recipe.getRecipeDescription() : macros,
                                        recipe.getRecipeImage(), // Usamos la URL tal cual viene
                                        recipe.getRecipeUrl(),
                                        R.drawable.ic_dieta,
                                        R.drawable.bg_login_modern,
                                        100
                                ));
                            }
                            actualizarAdaptador(carouselItemsList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDto<String>> call, Throwable t) {
                runOnUiThread(() -> {
                    carouselItemsList.clear();
                    carouselItemsList.add(new CarouselAdapter.CarouselItem(
                            "SIN CONEXIÓN",
                            "Recetas no disponibles",
                            "No se pudo conectar con el servidor de nutrición",
                            R.drawable.ic_dieta,
                            R.drawable.bg_login_modern,
                            0
                    ));
                    actualizarAdaptador(carouselItemsList);
                });
            }
        });

        // Actualizar el Widget de Nutrición (mantenemos la lógica de tu plan)
        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);
        if (usuarioId != -1) {
            apiService.obtenerUltimoPlan(usuarioId).enqueue(new Callback<ApiResponseDto<NutricionResponseDto>>() {
                @Override
                public void onResponse(Call<ApiResponseDto<NutricionResponseDto>> call, Response<ApiResponseDto<NutricionResponseDto>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                        NutricionResponseDto plan = response.body().getDatos();
                        runOnUiThread(() -> {
                            if (plan.getNombreDieta() != null && !plan.getNombreDieta().isEmpty()) {
                                tvNombrePlanWidget.setText(plan.getNombreDieta());
                            } else {
                                tvNombrePlanWidget.setText("Mi Plan Nutricional");
                            }
                            tvNombrePlanWidget.setTextColor(Color.WHITE);
                            tvCaloriasWidget.setTextColor(getResources().getColor(R.color.brand_pink));
                            actualizarProgresoMacros(plan);
                        });
                    } else {
                        runOnUiThread(() -> mostrarPlanNoAsignado());
                    }
                }

                @Override 
                public void onFailure(Call<ApiResponseDto<NutricionResponseDto>> call, Throwable t) {
                    runOnUiThread(() -> {
                        tvNombrePlanWidget.setText("Error de conexión");
                        tvNombrePlanWidget.setTextColor(Color.RED);
                    });
                }
            });
        }
    }

    private void mostrarPlanNoAsignado() {
        tvNombrePlanWidget.setText("Plan no asignado");
        tvNombrePlanWidget.setTextColor(Color.parseColor("#FFEB3B")); 
        tvCaloriasWidget.setText("--- kcal");
        tvCaloriasWidget.setTextColor(Color.parseColor("#FFEB3B"));
        progressProt.setProgress(100);
        progressCarb.setProgress(100);
        progressGras.setProgress(100);
    }

    private void actualizarProgresoMacros(NutricionResponseDto plan) {
        double p = 0, c = 0, g = 0, cal = 0;
        
        // 1. Sumar recetas sugeridas por la IA (almacenadas en macronutrientes)
        if (plan.getMacronutrientes() != null && plan.getMacronutrientes().getRecipes() != null && plan.getMacronutrientes().getRecipes().getRecipe() != null) {
            for (com.example.skynet.data.remote.dto.FatSecretRecipeDto recipe : plan.getMacronutrientes().getRecipes().getRecipe()) {
                if (recipe.getRecipeNutrition() != null) {
                    try {
                        String prot = recipe.getRecipeNutrition().getProtein();
                        String carb = recipe.getRecipeNutrition().getCarbohydrate();
                        String fat = recipe.getRecipeNutrition().getFat();
                        String cals = recipe.getRecipeNutrition().getCalories();
                        
                        if (prot != null) p += Double.parseDouble(prot);
                        if (carb != null) c += Double.parseDouble(carb);
                        if (fat != null) g += Double.parseDouble(fat);
                        if (cals != null) cal += Double.parseDouble(cals);
                    } catch (Exception ignored) {}
                }
            }
        }

        // 2. Sumar dietas confirmadas y comidas manuales
        List<com.example.skynet.data.remote.dto.ComidaDiariaResponseDto> todasLasComidas = new ArrayList<>();
        if (plan.getDietas() != null) todasLasComidas.addAll(plan.getDietas());
        if (plan.getComidas() != null) todasLasComidas.addAll(plan.getComidas());

        for (com.example.skynet.data.remote.dto.ComidaDiariaResponseDto item : todasLasComidas) {
            p += (item.getProteina() != null) ? item.getProteina() : 0;
            c += (item.getCarbohidratos() != null) ? item.getCarbohidratos() : 0;
            g += (item.getGrasas() != null) ? item.getGrasas() : 0;
            cal += (item.getCalorias() != null) ? item.getCalorias() : 0;
        }

        // Obtener objetivos del plan o usar valores por defecto (ej. 2000 kcal)
        int objetivoCal = (plan.getCaloriasObjetivo() != null) ? plan.getCaloriasObjetivo() : 2000;
        
        // Distribución ideal basada en el objetivo total: 30% P, 40% C, 30% G
        int objetivoP = (int) ((objetivoCal * 0.30) / 4);
        int objetivoC = (int) ((objetivoCal * 0.40) / 4);
        int objetivoG = (int) ((objetivoCal * 0.30) / 9);

        // Actualizar barras de progreso (calculando porcentaje de 0 a 100)
        progressProt.setProgress(objetivoP > 0 ? (int) ((p * 100) / objetivoP) : 0, true);
        progressCarb.setProgress(objetivoC > 0 ? (int) ((c * 100) / objetivoC) : 0, true);
        progressGras.setProgress(objetivoG > 0 ? (int) ((g * 100) / objetivoG) : 0, true);

        // Actualizar etiquetas de texto: Consumido / Objetivo
        if (tvProtLabel != null) tvProtLabel.setText(String.format(Locale.getDefault(), "P: %.0f/%dg", p, objetivoP));
        if (tvCarbLabel != null) tvCarbLabel.setText(String.format(Locale.getDefault(), "C: %.0f/%dg", c, objetivoC));
        if (tvGrasLabel != null) tvGrasLabel.setText(String.format(Locale.getDefault(), "G: %.0f/%dg", g, objetivoG));
        
        if (tvCaloriasWidget != null) {
            tvCaloriasWidget.setText(String.format(Locale.getDefault(), "%.0f / %d kcal", cal, objetivoCal));
        }
    }

    private void actualizarRendimientoSemanal() {
        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);
        if (usuarioId == -1) return;

        ApiService apiService = RetrofitClient.getApiService();

        // 1. Meta Semanal (Días asistidos vs Objetivo de 4 días)
        apiService.obtenerHistorialAccesos().enqueue(new Callback<ApiResponseDto<List<com.example.skynet.data.remote.dto.AccesoResponseDto>>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<List<com.example.skynet.data.remote.dto.AccesoResponseDto>>> call, Response<ApiResponseDto<List<com.example.skynet.data.remote.dto.AccesoResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.skynet.data.remote.dto.AccesoResponseDto> accesos = response.body().getDatos();
                    
                    Calendar cal = Calendar.getInstance();
                    cal.setFirstDayOfWeek(Calendar.MONDAY);
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    long inicioSemanaMs = cal.getTimeInMillis();

                    Set<String> diasEstaSemana = new HashSet<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    boolean[] dotsSemana = new boolean[7];

                    if (accesos != null) {
                        for (com.example.skynet.data.remote.dto.AccesoResponseDto acceso : accesos) {
                            try {
                                String fechaRaw = acceso.getFechaHoraEntrada();
                                String fechaStr = fechaRaw.contains("T") ? fechaRaw.split("T")[0] : fechaRaw.split(" ")[0];
                                Date fechaAcceso = sdf.parse(fechaStr);
                                
                                if (fechaAcceso != null && fechaAcceso.getTime() >= inicioSemanaMs) {
                                    diasEstaSemana.add(fechaStr);
                                    
                                    // Marcar el día en el tracker (L=0, D=6)
                                    Calendar tempCal = Calendar.getInstance();
                                    tempCal.setTime(fechaAcceso);
                                    int dayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK); // Dom=1, Lun=2...
                                    int index = (dayOfWeek == Calendar.SUNDAY) ? 6 : dayOfWeek - 2;
                                    if (index >= 0 && index < 7) dotsSemana[index] = true;
                                }
                            } catch (Exception ignored) {}
                        }
                    }

                    int diasAsistidos = diasEstaSemana.size();
                    int metaObjetivo = 4; // Puedes hacerlo dinámico después
                    actualizarUIActividadSemanal(diasAsistidos, metaObjetivo);
                    actualizarTrackerSemanal(dotsSemana);
                }
            }
            @Override public void onFailure(Call<ApiResponseDto<List<com.example.skynet.data.remote.dto.AccesoResponseDto>>> call, Throwable t) {}
        });

        // 2. Contador de Reservas Activas (Se mantiene)
        apiService.getMisReservas(usuarioId).enqueue(new Callback<ApiResponseDto<List<ReservaResponseDto>>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<List<ReservaResponseDto>>> call, Response<ApiResponseDto<List<ReservaResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                    List<ReservaResponseDto> reservas = response.body().getDatos();
                    int activas = 0;
                    for (ReservaResponseDto r : reservas) {
                        // Consideramos activa si no está cancelada (ajusta según tus estados)
                        if (!"CANCELADA".equalsIgnoreCase(r.getEstado())) {
                            activas++;
                        }
                    }
                    actualizarUIReservas(activas);
                }
            }
            @Override public void onFailure(Call<ApiResponseDto<List<ReservaResponseDto>>> call, Throwable t) {}
        });
    }

    private void actualizarTrackerSemanal(boolean[] diasCompletados) {
        runOnUiThread(() -> {
            diasCompletadosGlobal.clear();
            for (boolean d : diasCompletados) diasCompletadosGlobal.add(d);
            
            View[] dots = {dotL, dotM, dotMi, dotJ, dotV, dotS, dotD};
            int azulito = Color.parseColor("#0088CC");
            int divider = Color.parseColor("#E0E0E0");

            for (int i = 0; i < dots.length; i++) {
                if (dots[i] != null) {
                    dots[i].setBackgroundTintList(ColorStateList.valueOf(diasCompletados[i] ? azulito : divider));
                }
            }
            updateWeeklyPerformanceComposeContent();
        });
    }

    private void actualizarUIActividadSemanal(int asistidos, int meta) {
        runOnUiThread(() -> {
            int porcentaje = (meta > 0) ? (asistidos * 100) / meta : 0;
            if (porcentaje > 100) porcentaje = 100;
            
            actividadPorcentajeGlobal = porcentaje;
            
            if (tvActividadPorcentaje != null) {
                // Cambiamos el texto de "0%" a "2/4" para que sea más claro
                tvActividadPorcentaje.setText(asistidos + "/" + meta);
                tvActividadPorcentaje.setTextColor(Color.parseColor("#00E5FF")); // Cyan neón
            }
            
            if (progressCircularStat != null) {
                progressCircularStat.setProgress(porcentaje, true);
                progressCircularStat.setIndicatorColor(Color.parseColor("#00E5FF"));
            }
            
            updateWeeklyPerformanceComposeContent();
        });
    }

    private void actualizarUIReservas(int activas) {
        runOnUiThread(() -> {
            reservasCountGlobal = activas;
            
            if (tvReservasValor != null) {
                tvReservasValor.setText(String.valueOf(activas));
            }
            
            int porcentaje = Math.min((activas * 100) / 5, 100);
            if (progressReservas != null) {
                progressReservas.setProgress(porcentaje, true);
            }

            // Colores Neon Dinámicos
            int color;
            if (activas >= 3) {
                color = Color.parseColor("#CD0277"); // Rosa (Mucho compromiso)
            } else if (activas >= 1) {
                color = Color.parseColor("#FFD600"); // Amarillo (Activo)
            } else {
                color = Color.parseColor("#4FC3F7"); // Azul (Sin reservas)
            }
            
            if (tvReservasValor != null) tvReservasValor.setTextColor(color);
            if (progressReservas != null) progressReservas.setIndicatorColor(color);

            updateWeeklyPerformanceComposeContent();
        });
    }

    private void calcularRachaActual(long usuarioId) {
        RetrofitClient.getApiService().obtenerHistorialAccesos().enqueue(new Callback<ApiResponseDto<List<com.example.skynet.data.remote.dto.AccesoResponseDto>>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<List<com.example.skynet.data.remote.dto.AccesoResponseDto>>> call, Response<ApiResponseDto<List<com.example.skynet.data.remote.dto.AccesoResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.skynet.data.remote.dto.AccesoResponseDto> accesos = response.body().getDatos();
                    int racha = 0;
                    if (accesos != null && !accesos.isEmpty()) {
                        Set<String> diasEntrenados = new HashSet<>();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        
                        for (com.example.skynet.data.remote.dto.AccesoResponseDto acceso : accesos) {
                            String fechaFull = acceso.getFechaHoraEntrada();
                            if (fechaFull != null) {
                                try {
                                    // Extraer solo la parte de la fecha (yyyy-MM-dd)
                                    String fecha = fechaFull.contains("T") ? fechaFull.split("T")[0] : 
                                                  (fechaFull.contains(" ") ? fechaFull.split(" ")[0] : fechaFull);
                                    diasEntrenados.add(fecha.trim());
                                } catch (Exception e) {
                                    android.util.Log.e("Racha", "Error parseando fecha: " + fechaFull);
                                }
                            }
                        }

                        Calendar cal = Calendar.getInstance();
                        // IMPORTANTE: Si tus logs dicen 2026, asegúrate que cal.getTime() también sea 2026
                        String hoy = sdf.format(cal.getTime());
                        
                        // Si hoy no hay registro, probamos desde ayer
                        if (!diasEntrenados.contains(hoy)) {
                            cal.add(Calendar.DAY_OF_YEAR, -1);
                        }

                        while (diasEntrenados.contains(sdf.format(cal.getTime()))) {
                            racha++;
                            cal.add(Calendar.DAY_OF_YEAR, -1);
                            if (racha > 1000) break; // Seguridad
                        }
                    }
                    
                    final int finalRacha = racha;
                    runOnUiThread(() -> {
                        if (tvStreakCount != null) {
                            tvStreakCount.setText(String.valueOf(finalRacha));
                            if (finalRacha > 0) {
                                tvStreakCount.animate().scaleX(1.3f).scaleY(1.3f).setDuration(200).withEndAction(() -> 
                                    tvStreakCount.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                                ).start();
                            }
                        }
                    });
                }
            }
            @Override 
            public void onFailure(Call<ApiResponseDto<List<com.example.skynet.data.remote.dto.AccesoResponseDto>>> call, Throwable t) {
                android.util.Log.e("API_ERROR", "Fallo al obtener historial", t);
            }
        });
    }


    private void actualizarAdaptador(List<CarouselAdapter.CarouselItem> items) {
        runOnUiThread(() -> {
            if (carouselAdapter != null) {
                carouselAdapter.updateItems(new ArrayList<>(items));
            }
        });
    }

    private void setupModernMenu() {
        View menuView = slidingRootNav.getLayout().findViewById(R.id.menu_items_container).getRootView();
        
        ivHeaderPhoto = menuView.findViewById(R.id.img_logo_header_modern);
        TextView tvNombre = menuView.findViewById(R.id.tv_nombre_header_modern);
        
        // Actualizar datos del header en el menú
        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        if (tvNombre != null) {
            tvNombre.setText(prefs.getString("nombre_usuario", "Usuario GymCrush"));
        }

        // Verificar si es ADMIN para mostrar gestión de staff
        java.util.Set<String> roles = prefs.getStringSet("roles", new java.util.HashSet<>());
        boolean isAdmin = roles.contains("ROLE_ADMIN") || roles.contains("ADMIN");
        boolean isStaff = roles.contains("ROLE_STAFF") || roles.contains("STAFF") || isAdmin;

        View menuAdminDashboard = menuView.findViewById(R.id.menu_admin_dashboard);
        if (menuAdminDashboard != null) {
            menuAdminDashboard.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
            menuAdminDashboard.setOnClickListener(v -> {
                slidingRootNav.closeMenu();
                cargarFragmento(new AdminDashboardFragment());
            });
        }

        View menuStaffAdmin = menuView.findViewById(R.id.menu_staff_admin);
        if (menuStaffAdmin != null) {
            menuStaffAdmin.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
            menuStaffAdmin.setOnClickListener(v -> {
                slidingRootNav.closeMenu();
                cargarFragmento(new com.example.skynet.ui.staff.StaffFragment());
            });
        }

        View menuScanner = menuView.findViewById(R.id.menu_escanear_qr);
        if (menuScanner != null) {
            menuScanner.setVisibility(isStaff ? View.VISIBLE : View.GONE);
            menuScanner.setOnClickListener(v -> {
                slidingRootNav.closeMenu();
                cargarFragmento(new com.example.skynet.ui.scanner.ScannerFragment());
            });
        }

        menuView.findViewById(R.id.menu_qr).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            cargarFragmento(new com.example.skynet.ui.acceso.AccesoFragment());
        });
        menuView.findViewById(R.id.menu_entrenamiento).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            cargarFragmento(new EntrenamientoFragment());
        });
        menuView.findViewById(R.id.menu_dieta).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            cargarFragmento(new DietaFragment());
        });
        menuView.findViewById(R.id.menu_reservas).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            cargarFragmento(new ReservasFragment());
        });
        menuView.findViewById(R.id.menu_salud).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            Intent intent = new Intent(DesarrolloActivity.this, com.example.skynet.ui.salud.RegistroSaludActivity.class);
            startActivityForResult(intent, 102);
        });
        menuView.findViewById(R.id.menu_suplementacion).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            cargarFragmento(new SuplementacionFragment());
        });
        menuView.findViewById(R.id.menu_equipo).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            cargarFragmento(new EquipoFragment());
        });
        menuView.findViewById(R.id.menu_contacto).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            cargarFragmento(new ContactoFragment());
        });
        menuView.findViewById(R.id.menu_espacio).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            cargarFragmento(new EspacioFragment());
        });
        menuView.findViewById(R.id.menu_salir).setOnClickListener(v -> {
            slidingRootNav.closeMenu();
            mostrarDialogoCerrarSesion();
        });
    }

    private void cargarDatosYFotoPerfil() {
        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);

        if (usuarioId == -1) return;

        ApiService apiService = RetrofitClient.getApiService();

        // Llamamos al endpoint que devuelve una lista de perfiles y filtramos por usuarioId en el backend
        // o si tienes un endpoint específico para el perfil del usuario logueado.
        // Según tu backend, el endpoint /api/v1/perfil devuelve todos, y /api/v1/perfil/{id} devuelve por ID de PERFIL.
        // Usamos getPerfilByUsuarioId para asegurar que obtenemos el perfil del usuario logueado.
        apiService.getPerfilByUsuarioId(usuarioId).enqueue(new Callback<ApiResponseDto<PerfilResponseDto>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<PerfilResponseDto>> call, Response<ApiResponseDto<PerfilResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PerfilResponseDto perfil = response.body().getDatos();

                    // Guardar perfil_id para futuros usos si es distinto al user_id
                    if (perfil.getId() != null) {
                        prefs.edit().putLong("perfil_id", perfil.getId()).apply();
                    }

                    // 1. Actualizar Saludo con nombre real (si no es genérico)
                    TextView tvSaludo = findViewById(R.id.tvSaludo);
                    if (tvSaludo != null && perfil.getNombre() != null) {
                        String nombrePerfil = perfil.getNombre();
                        // Si el perfil tiene un nombre genérico, preferimos el que ya tenemos (probablemente de Google)
                        if (nombrePerfil.equalsIgnoreCase("admin") || nombrePerfil.equalsIgnoreCase("user")) {
                            nombrePerfil = prefs.getString("nombre_usuario", nombrePerfil);
                        }
                        tvSaludo.setText("¡A tope, " + nombrePerfil.replace("GymCrush", "").trim() + "!");
                    }

                    // 2. Cargar Foto si existe
                    if (perfil.getFoto() != null && !perfil.getFoto().isEmpty()) {
                        try {
                            byte[] decodedString = Base64.decode(perfil.getFoto(), Base64.DEFAULT);
                            // Foto en la pantalla principal (Home)
                            Glide.with(DesarrolloActivity.this)
                                    .load(decodedString)
                                    .circleCrop()
                                    .into(profileImage);

                            // Foto en el Header del Menú Lateral (Moderno)
                            if (ivHeaderPhoto != null) {
                                Glide.with(DesarrolloActivity.this)
                                        .load(decodedString)
                                        .circleCrop()
                                        .into(ivHeaderPhoto);
                            }
                        } catch (Exception e) {
                            Log.e("IMAGE_ERROR", "Error al decodificar imagen: " + e.getMessage());
                        }
                    }
                } else {
                    Log.e("API_ERROR", "Error en respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDto<PerfilResponseDto>> call, Throwable t) {
                Log.e("API_ERROR", "Fallo al conectar: " + t.getMessage());
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        drawer = findViewById(R.id.drawer_layout_root);
        
        cargarDatosYFotoPerfil();
        cargarDatosDinamicosCarrusel();
        actualizarRendimientoSemanal();
    }

    @OptIn(markerClass = UnstableApi.class)
    private void initReproductor(CarouselAdapter.CarouselItem item, String subtitulo, Integer duracion, String etiquetaAzul) {
        cargarFragmento(ReproductorFragment.newInstance(
                item.getId(),
                item.getImageUrl(), // o getUrlVideo() si es un video
                item.getTitle(),
                item.getFullDescription() != null ? item.getFullDescription() : item.getDescription(),
                subtitulo,
                duracion,
                etiquetaAzul,
                null
        ));
    }

    private void cargarFragmento(Fragment fragment) {
        homeContent.setVisibility(View.GONE);
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void mostrarHome() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        findViewById(R.id.fragment_container).setVisibility(View.GONE);
        homeContent.setVisibility(View.VISIBLE);
        drawer = findViewById(R.id.drawer_layout_root);

        cargarDatosYFotoPerfil();
        cargarDatosDinamicosCarrusel();
        actualizarRendimientoSemanal();
    }

    private void mostrarDialogoQR() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_qr_gym);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        ImageView ivQR = dialog.findViewById(R.id.ivQrCode);
        ProgressBar progress = dialog.findViewById(R.id.progressQr);
        if (progress != null) progress.setVisibility(View.VISIBLE);

        // Obtener un token válido del servidor en lugar de uno aleatorio
        RetrofitClient.getApiService().generarTokenQr().enqueue(new Callback<ApiResponseDto<String>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<String>> call, Response<ApiResponseDto<String>> response) {
                if (progress != null) progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getDatos();
                    try {
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        BitMatrix bitMatrix = multiFormatWriter.encode(token, BarcodeFormat.QR_CODE, 500, 500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        ivQR.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DesarrolloActivity.this, "Error al generar token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDto<String>> call, Throwable t) {
                if (progress != null) progress.setVisibility(View.GONE);
                Toast.makeText(DesarrolloActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.findViewById(R.id.btnCerrarQr).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void mostrarDialogoCerrarSesion() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_custom_exit);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        dialog.findViewById(R.id.btn_confirmar_exit).setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        dialog.findViewById(R.id.btn_cancelar_exit).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        
        // Aplicar estilo oscuro al mapa para que combine con la App
        try {
            boolean success = map.setMapStyle(
                com.google.android.gms.maps.model.MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style));
            if (!success) android.util.Log.e("MAP", "Error al aplicar estilo oscuro.");
        } catch (android.content.res.Resources.NotFoundException e) {
            android.util.Log.e("MAP", "No se encontró el recurso de estilo: R.raw.map_style");
        }

        // Lista de sedes aleatorias de Skynet Club
        LatLng[] sedes = {
            new LatLng(39.4699, -0.3763),  // Valencia
            new LatLng(40.4168, -3.7038),  // Madrid
            new LatLng(41.3851, 2.1734),   // Barcelona
            new LatLng(37.3891, -5.9845),  // Sevilla
            new LatLng(36.7213, -4.4214)   // Málaga
        };

        // Seleccionar una sede al azar
        LatLng randomSede = sedes[new java.util.Random().nextInt(sedes.length)];
        
        map.addMarker(new MarkerOptions()
            .position(randomSede)
            .title("Skynet Club")
            .snippet("Tu centro de entrenamiento inteligente")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(randomSede, 15f));
        
        // Habilitar controles de UI para que se vea más profesional
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
    }
    private void iniciarWebSockets(long usuarioId) {
        if (usuarioId == -1) return;

        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");

        stompManager = new com.example.skynet.data.remote.StompManager();
        String url = Config.WS_URL;
        
        stompManager.connect(url, usuarioId, token, notification -> {
            runOnUiThread(() -> {
                reproducirAnimacionNotificacion();
                Toast.makeText(this, "🔔 " + notification.getTitulo() + ": " + notification.getMensaje(), Toast.LENGTH_LONG).show();
                mostrarNotificacionPush(notification.getTitulo(), notification.getMensaje());
                notificacionesSesion.add(0, notification);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof NotificacionesFragment) {
                    ((NotificacionesFragment) currentFragment).onNuevaNotificacionRecibida(notification);
                }
            });
        }, occupancy -> {
            runOnUiThread(() -> actualizarUIAforo(occupancy));
        }, payload -> {
            runOnUiThread(() -> {
                try {
                    Type listType = new TypeToken<List<FriendAdapter.ActiveFriendDto>>(){}.getType();
                    List<FriendAdapter.ActiveFriendDto> activeFriends = new Gson().fromJson(payload, listType);
                    if (friendAdapter != null) {
                        friendAdapter.setFriends(activeFriends);
                    }
                } catch (Exception e) {
                    Log.e("STOMP_FRIENDS", "Error parsing active friends", e);
                }
            });
        }, connected -> {
            runOnUiThread(() -> {
                if (wsStatusIndicator != null) {
                    wsStatusIndicator.setBackgroundResource(connected ? 
                        R.drawable.bg_circle_green_pulse : R.drawable.bg_indicator_red);
                }
            });
        });

        // Carga inicial de aforo por REST
        RetrofitClient.getApiService().obtenerAforoActual().enqueue(new Callback<ApiResponseDto<Long>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<Long>> call, Response<ApiResponseDto<Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    actualizarUIAforo(response.body().getDatos());
                }
            }
            @Override public void onFailure(Call<ApiResponseDto<Long>> call, Throwable t) {}
        });
    }

    private void actualizarUIAforo(Long count) {
        if (tvAforoPorcentaje == null || statusAforoIndicator == null) return;
        
        long personas = (count != null) ? count : 0;
        int capacidadMaxima = 150; 
        
        // Usar double para que la división no sea 0 con pocas personas (ej: 1/150)
        int porcentaje = (int) Math.ceil((personas * 100.0) / capacidadMaxima);
        
        android.util.Log.d("AFORO_DEBUG", "Personas recibidas: " + personas + " -> Porcentaje: " + porcentaje + "%");
        
        runOnUiThread(() -> {
            tvAforoPorcentaje.setText(porcentaje + "%");
            
            int color;
            if (porcentaje < 50) color = android.graphics.Color.parseColor("#4CAF50"); 
            else if (porcentaje < 80) color = android.graphics.Color.parseColor("#FFEB3B"); 
            else color = android.graphics.Color.parseColor("#F44336"); 
            
            statusAforoIndicator.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
            
            statusAforoIndicator.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction(() -> 
                statusAforoIndicator.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
            ).start();
        });
    }

    private void updateWeightChart() {
        if (weightLineChart == null) return;

        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);
        if (usuarioId == -1) return;

        RetrofitClient.getApiService().getEvolucionPeso(usuarioId).enqueue(new Callback<ApiResponseDto<List<EvolucionPesoDto>>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<List<EvolucionPesoDto>>> call, Response<ApiResponseDto<List<EvolucionPesoDto>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                    List<EvolucionPesoDto> evolucion = response.body().getDatos();
                    runOnUiThread(() -> {
                        if (tvWeightChartError != null) tvWeightChartError.setVisibility(View.GONE);
                        weightLineChart.setVisibility(View.VISIBLE);
                        setupWeightChartFromEvolucion(evolucion);
                    });
                } else {
                    runOnUiThread(() -> {
                        if (tvWeightChartError != null) tvWeightChartError.setVisibility(View.VISIBLE);
                        weightLineChart.setVisibility(View.GONE);
                    });
                }
            }
            @Override
            public void onFailure(Call<ApiResponseDto<List<EvolucionPesoDto>>> call, Throwable t) {
                runOnUiThread(() -> {
                    if (tvWeightChartError != null) {
                        tvWeightChartError.setText("Error de red al cargar la gráfica. 🔄");
                        tvWeightChartError.setVisibility(View.VISIBLE);
                    }
                    weightLineChart.setVisibility(View.GONE);
                });
            }
        });
    }

    private void setupWeightChartFromEvolucion(List<EvolucionPesoDto> evolucion) {
        if (evolucion == null || evolucion.isEmpty()) {
            if (tvWeightChartError != null) {
                tvWeightChartError.setText("No hay datos de evolución de peso registrados. ¡Añade tu primer peso! 📈");
                tvWeightChartError.setVisibility(View.VISIBLE);
            }
            weightLineChart.setVisibility(View.GONE);
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        for (int i = 0; i < evolucion.size(); i++) {
            EvolucionPesoDto dato = evolucion.get(i);
            if (dato.getPeso() != null) {
                entries.add(new Entry(i, dato.getPeso().floatValue()));
                dates.add(dato.getFecha() != null ? dato.getFecha().substring(5, 10) : ""); // MM-dd
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Evolución de Peso");
        dataSet.setColor(Color.parseColor("#7C4DFF")); // Púrpura vibrante
        dataSet.setCircleColor(Color.parseColor("#7C4DFF"));
        dataSet.setLineWidth(3.5f);
        dataSet.setCircleRadius(6f);
        dataSet.setCircleHoleRadius(3f);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleHoleColor(Color.WHITE);
        dataSet.setValueTextColor(Color.parseColor("#444444")); // Texto oscuro
        dataSet.setValueTextSize(11f);
        dataSet.setDrawFilled(true);
        
        // Efecto de degradado púrpura a transparente
        dataSet.setFillDrawable(new android.graphics.drawable.GradientDrawable(
                android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#4D7C4DFF"), Color.TRANSPARENT}
        ));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        weightLineChart.setData(lineData);

        // Configuración visual del Chart
        weightLineChart.getDescription().setEnabled(false);
        weightLineChart.getLegend().setEnabled(false);
        weightLineChart.setDrawGridBackground(false);
        weightLineChart.setTouchEnabled(true);
        weightLineChart.setPinchZoom(true);
        weightLineChart.setBackgroundColor(Color.TRANSPARENT);

        XAxis xAxis = weightLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.parseColor("#666666")); // Gris para legibilidad
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(Color.parseColor("#DDDDDD"));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setGranularity(1f);

        YAxis leftAxis = weightLineChart.getAxisLeft();
        leftAxis.setTextColor(Color.parseColor("#666666"));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#EEEEEE")); // Rejilla suave
        leftAxis.setDrawAxisLine(false);

        weightLineChart.getAxisRight().setEnabled(false);
        weightLineChart.animateX(1000);
        weightLineChart.invalidate();
    }

    private void mostrarDialogoLogSalud() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_manual_log_salud);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        com.google.android.material.textfield.TextInputEditText etPeso = dialog.findViewById(R.id.etManualPeso);
        com.google.android.material.textfield.TextInputEditText etEstatura = dialog.findViewById(R.id.etManualEstatura);
        LottieAnimationView lottieSuccess = dialog.findViewById(R.id.lottieSuccess);
        View layoutForm = dialog.findViewById(R.id.layoutForm);

        dialog.findViewById(R.id.btnGuardarSalud).setOnClickListener(v -> {
            String pesoStr = etPeso.getText().toString();
            String estaturaStr = etEstatura.getText().toString();

            if (pesoStr.isEmpty() || estaturaStr.isEmpty()) {
                Toast.makeText(this, "Completa los datos", Toast.LENGTH_SHORT).show();
                return;
            }

            double peso = Double.parseDouble(pesoStr);
            double estatura = Double.parseDouble(estaturaStr);

            SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
            long usuarioId = prefs.getLong("user_id", -1);

            com.example.skynet.data.remote.dto.SaludRequestDto request = new com.example.skynet.data.remote.dto.SaludRequestDto();
            request.setPeso(peso);
            request.setEstatura(estatura);
            request.setNivelActividad("MODERADO");

            RetrofitClient.getApiService().registrarSalud(usuarioId, request).enqueue(new Callback<ApiResponseDto<com.example.skynet.data.remote.dto.SaludResponseDto>>() {
                @Override
                public void onResponse(Call<ApiResponseDto<com.example.skynet.data.remote.dto.SaludResponseDto>> call, Response<ApiResponseDto<com.example.skynet.data.remote.dto.SaludResponseDto>> response) {
                    if (response.isSuccessful()) {
                        layoutForm.setVisibility(View.GONE);
                        lottieSuccess.setVisibility(View.VISIBLE);
                        lottieSuccess.playAnimation();
                        
                        lottieSuccess.addAnimatorListener(new android.animation.AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(android.animation.Animator animation) {
                                updateWeightChart();
                                dialog.dismiss();
                            }
                        });
                    }
                }
                @Override public void onFailure(Call<ApiResponseDto<com.example.skynet.data.remote.dto.SaludResponseDto>> call, Throwable t) {
                    Toast.makeText(DesarrolloActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.findViewById(R.id.btnCerrarManual).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void mostrarBroadcast(String titulo, String contenido) {
        if (layoutBroadcast != null && tvBroadcastTitle != null && tvBroadcastContent != null) {
            tvBroadcastTitle.setText(titulo);
            tvBroadcastContent.setText(contenido);
            layoutBroadcast.setVisibility(View.VISIBLE);
            
            // Auto-ocultar después de 10 segundos
            layoutBroadcast.postDelayed(() -> layoutBroadcast.setVisibility(View.GONE), 10000);
        }
    }

    public void reproducirAnimacionNotificacion() {
        if (notificationsAnim != null) {
            notificationsAnim.playAnimation();
        }
        mostrarBadgeNotificacion();
    }

    public void crearNotificacionManual(String titulo, String mensaje) {
        reproducirAnimacionNotificacion();
        
        // 1. Mostrar Toast visual
        runOnUiThread(() -> {
            Toast.makeText(this, "🔔 " + titulo + "\n" + mensaje, Toast.LENGTH_LONG).show();
        });

        // 2. Enviar notificación al sistema (Barra de estado)
        mostrarNotificacionPush(titulo, mensaje);

        // 3. Crear el objeto DTO para la UI
        com.example.skynet.data.remote.dto.NotificacionResponseDto notif = new com.example.skynet.data.remote.dto.NotificacionResponseDto();
        notif.setTitulo(titulo);
        notif.setMensaje(mensaje);
        notif.setLeido(false);
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
        notif.setFecha(sdf.format(new java.util.Date()));

        // Guardar en la sesión para que aparezca al abrir el fragmento
        notificacionesSesion.add(0, notif);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof NotificacionesFragment) {
            ((NotificacionesFragment) currentFragment).onNuevaNotificacionRecibida(notif);
        }
    }

    private void mostrarNotificacionPush(String titulo, String mensaje) {
        // Verificar permiso para Android 13+
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) 
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Si no tiene permiso, lo pedimos (aunque en un flujo ideal se pediría al inicio)
                androidx.core.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
                return;
            }
        }

        String channelId = "gymcrush_notifications";
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        android.app.NotificationChannel channel = new android.app.NotificationChannel(
                channelId, "Notificaciones GYMCrush", android.app.NotificationManager.IMPORTANCE_HIGH);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }

        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notifications) // Corregido el nombre del icono
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    public void mostrarBadgeNotificacion() {
        if (notificationBadge != null) {
            notificationBadge.setVisibility(View.VISIBLE);
        }
    }

    public void ocultarBadgeNotificacion() {
        if (notificationBadge != null) {
            notificationBadge.setVisibility(View.GONE);
        }
    }

    private void simulateActiveFriendsUpdate() {
        List<FriendAdapter.ActiveFriendDto> mockFriends = java.util.Arrays.asList(
            new FriendAdapter.ActiveFriendDto(1L, "Alex Pro", null, "Press Banca"),
            new FriendAdapter.ActiveFriendDto(2L, "Maria Fit", null, "Sentadillas"),
            new FriendAdapter.ActiveFriendDto(3L, "Dani Iron", null, "Cardio")
        );
        if (friendAdapter != null) {
            friendAdapter.setFriends(mockFriends);
            Toast.makeText(this, "Simulando amigos activos...", Toast.LENGTH_SHORT).show();
        }
    }

    public List<com.example.skynet.data.remote.dto.NotificacionResponseDto> getNotificacionesSesion() {
        return notificacionesSesion;
    }

    @Override
    protected void onDestroy() {
        if (stompManager != null) {
            stompManager.disconnect();
        }
        super.onDestroy();
    }
}