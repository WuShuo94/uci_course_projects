################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../project1/dSelect.cpp \
../project1/quickSelect.cpp \
../project1/test.cpp \
../project1/timer.cpp 

C_SRCS += \
../project1/dshrandom.c 

OBJS += \
./project1/dSelect.o \
./project1/dshrandom.o \
./project1/quickSelect.o \
./project1/test.o \
./project1/timer.o 

CPP_DEPS += \
./project1/dSelect.d \
./project1/quickSelect.d \
./project1/test.d \
./project1/timer.d 

C_DEPS += \
./project1/dshrandom.d 


# Each subdirectory must supply rules for building sources it contributes
project1/%.o: ../project1/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

project1/%.o: ../project1/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


