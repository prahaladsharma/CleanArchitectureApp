

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// MARK: - Color Palette
private val ScreenBackground = Color(0xFFF3F7FC)
private val CardBackground = Color(0xFFFFFFFF)
private val PrimaryBlue = Color(0xFF0D55C7)
private val LightBlue = Color(0xFF007BFF)
private val NeonCyan = Color(0xFF53D4FF)
private val SoftBlueTrack = Color(0xFFE8F1FC)
private val SuccessGreen = Color(0xFF00B050)
private val WarningAmber = Color(0xFFFDB515)
private val MetricPurple = Color(0xFF8E44AD)
private val InactiveGray = Color(0xFFD6DFEC)
private val TextDark = Color(0xFF141C2B)
private val TextMuted = Color(0xFF6C7B93)

// MARK: - Main Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehouseDashboardScreen(
    taskId: String = "PICK-2048",
    warehouseCode: String = "WH-01",
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onViewDetailsClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Task ID",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                        Text(
                            text = taskId,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    WarehouseDropdownBadge(warehouseCode = warehouseCode)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                )
            )
        },
        containerColor = ScreenBackground,
        bottomBar = {
            BottomActionSection(
                onContinueClick = onContinueClick,
                onViewDetailsClick = onViewDetailsClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OrderPickingSummaryCard(
                title = "Order picking",
                locationSub = "Aisle A-12  •  Zone 03",
                status = "In progress",
                current = 13,
                total = 20
            )

            LocationProgressCard(
                completed = 13,
                total = 20,
                etaMinutes = 8
            )

            TaskMilestonesCard(
                startTime = "09:12",
                currentProgress = "13/20"
            )

            PickerPerformanceCard(
                onTargetRatio = 0.45f,
                belowTargetRatio = 0.18f,
                rate = "42 items/hr",
                accuracy = "99.2%"
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// MARK: - Top Bar Warehouse Badge
@Composable
private fun WarehouseDropdownBadge(warehouseCode: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White.copy(alpha = 0.15f),
        modifier = Modifier.padding(end = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { }
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = warehouseCode,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// MARK: - Card 1: Order Picking Summary
@Composable
private fun OrderPickingSummaryCard(
    title: String,
    locationSub: String,
    status: String,
    current: Int,
    total: Int
) {
    DashboardCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(SoftBlueTrack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text(
                    text = locationSub,
                    fontSize = 13.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SoftBlueTrack.copy(alpha = 0.7f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.12f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = null,
                            tint = LightBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = status,
                            color = PrimaryBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            CircularPercentageProgress(
                progress = current.toFloat() / total.toFloat(),
                current = current,
                total = total,
                size = 118.dp
            )
        }
    }
}

@Composable
private fun CircularPercentageProgress(
    progress: Float,
    current: Int,
    total: Int,
    size: Dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "progress_anim"
    )

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = 11.dp.toPx()
            val canvasSize = this.size.minDimension
            val radius = (canvasSize - strokeWidth) / 2f
            val centerOffset = Offset(canvasSize / 2f, canvasSize / 2f)

            // Background Track
            drawCircle(
                color = SoftBlueTrack,
                radius = radius,
                center = centerOffset,
                style = Stroke(width = strokeWidth)
            )

            // Active Arc
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to LightBlue,
                    0.7f to PrimaryBlue,
                    1.0f to LightBlue
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                size = Size(canvasSize - strokeWidth, canvasSize - strokeWidth),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
            Text(
                text = "$current of $total\nlocations",
                fontSize = 10.sp,
                color = TextMuted,
                lineHeight = 12.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// MARK: - Card 2: Location Progress
@Composable
private fun LocationProgressCard(
    completed: Int,
    total: Int,
    etaMinutes: Int
) {
    val remaining = (total - completed).coerceAtLeast(0)
    val fraction = (completed.toFloat() / total.toFloat()).coerceIn(0f, 1f)

    DashboardCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Location progress",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "$remaining locations remaining",
                        fontSize = 12.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Glowing segmented track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(13.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SoftBlueTrack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(LightBlue, NeonCyan)
                            )
                        )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$completed of $total locations completed",
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "~$etaMinutes min left",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
        }
    }
}

// MARK: - Card 3: Horizontal Milestones
@Composable
private fun TaskMilestonesCard(
    startTime: String,
    currentProgress: String
) {
    DashboardCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Milestone 1: Started
            MilestoneStep(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                title = "Started",
                subtitle = startTime,
                subtitleColor = TextDark
            )

            MilestoneConnector(
                isDashed = false,
                lineColor = SuccessGreen,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 18.dp)
            )

            // Milestone 2: Picking
            MilestoneStep(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(LightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                },
                title = "Picking",
                subtitle = currentProgress,
                subtitleColor = PrimaryBlue,
                subtitleBold = true
            )

            MilestoneConnector(
                isDashed = true,
                lineColor = InactiveGray,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 18.dp)
            )

            // Milestone 3: Packing
            MilestoneStep(
                icon = {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, InactiveGray, CircleShape)
                            .background(CardBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = null,
                            tint = TextMuted.copy(alpha = 0.5f),
                            modifier = Modifier.size(19.dp)
                        )
                    }
                },
                title = "Packing",
                subtitle = "Pending",
                subtitleColor = TextMuted
            )
        }
    }
}

@Composable
private fun MilestoneStep(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    subtitleColor: Color,
    subtitleBold: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(68.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            text = subtitle,
            fontSize = 12.sp,
            fontWeight = if (subtitleBold) FontWeight.ExtraBold else FontWeight.Medium,
            color = subtitleColor
        )
    }
}

@Composable
private fun MilestoneConnector(
    isDashed: Boolean,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.height(2.dp)) {
        val pathEffect = if (isDashed) PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) else null
        drawLine(
            color = lineColor,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 2.dp.toPx(),
            pathEffect = pathEffect
        )
    }
}

// MARK: - Card 4: Picker Performance
@Composable
private fun PickerPerformanceCard(
    onTargetRatio: Float,
    belowTargetRatio: Float,
    rate: String,
    accuracy: String
) {
    DashboardCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Picker performance",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Based on recent picks",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = TextMuted,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Multi-segment progress bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(SoftBlueTrack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(onTargetRatio)
                        .background(SuccessGreen)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(belowTargetRatio)
                        .background(WarningAmber)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight((1f - onTargetRatio - belowTargetRatio).coerceAtLeast(0f))
                        .background(SoftBlueTrack)
                )
            }

            // Performance Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LegendIndicator(color = SuccessGreen, text = "On target")
                LegendIndicator(color = WarningAmber, text = "Below target")
                LegendIndicator(color = MetricPurple, text = "Above average")
            }

            Divider(color = ScreenBackground, thickness = 1.dp)

            // Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricEntry(
                    icon = Icons.Outlined.BarChart,
                    label = "Rate",
                    value = rate,
                    iconTint = PrimaryBlue,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .width(1.dp)
                        .background(InactiveGray.copy(alpha = 0.5f))
                )

                MetricEntry(
                    icon = Icons.Outlined.CheckCircle,
                    label = "Accuracy",
                    value = accuracy,
                    iconTint = PrimaryBlue,
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun LegendIndicator(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            color = TextDark,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MetricEntry(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SoftBlueTrack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )
        }
    }
}

// MARK: - Bottom Persistent Actions
@Composable
private fun BottomActionSection(
    onContinueClick: () -> Unit,
    onViewDetailsClick: () -> Unit
) {
    Surface(
        color = CardBackground,
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Gradient Continue Button
            Button(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp), ambientColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(PrimaryBlue, LightBlue)
                            )
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Continue Picking",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Outline / Secondary Button
            OutlinedButton(
                onClick = onViewDetailsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, InactiveGray),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = TextDark,
                    containerColor = CardBackground
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Description,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = "View task details",
                        color = TextDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// MARK: - Reusable Card Container
@Composable
private fun DashboardCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WarehouseDashboardPreview() {
    MaterialTheme {
        WarehouseDashboardScreen()
    }
}
