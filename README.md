# FileArchiver

**FileArchiver** is a robust tool designed to safely archive outdated data from very large datasets (Terabyte size) and efficiently filter geo-data for mapping purposes. Developed for **Deutsche Bahn AG**, it addresses the challenges of managing and processing extensive geographical data.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [Archiving Data](#archiving-data)
  - [Filtering Geo-Data](#filtering-geo-data)
- [How It Works](#how-it-works)
- [Support](#support)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)
- [Contact](#contact)

## Features

- **Safe Archiving**: Securely archives outdated data from massive datasets to optimize storage and improve system performance.
- **Efficient Geo-Data Filtering**: Filters geo-data based on location codes and kilometer markers for precise mapping and analysis.
- **User-Friendly GUI**: Intuitive graphical user interface built with JavaFX for ease of use.
- **Progress Monitoring**: Real-time progress bars and log messages to monitor ongoing processes.
- **Multi-threading**: Uses multi-threading to perform operations without freezing the user interface.
- **Customizable Parameters**: Users can specify search paths, save directories, and filtering criteria to suit their needs.

## Getting Started

Follow these instructions to set up FileArchiver on your local machine.

### Prerequisites

- **Java Development Kit (JDK) 8 or higher**
- **JavaFX SDK**: Required for building and running the JavaFX application.
- **Git**: To clone the repository.
- **IDE** (Optional but recommended): Such as IntelliJ IDEA or Eclipse, with JavaFX support.

### Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/FileArchiver.git
   ```

2. **Import the Project**

   Open your IDE and import the project as a Maven or Gradle project if build scripts are provided. Ensure that JavaFX libraries are properly configured.

3. **Configure JavaFX**

   - For **IntelliJ IDEA**:
     - Go to **Project Structure** -> **Libraries** and add the JavaFX SDK.
     - Update the VM options to include the JavaFX modules.

   - For **Eclipse**:
     - Install the e(fx)clipse plugin.
     - Configure the build path to include JavaFX libraries.

4. **Build the Project**

   Build the project to resolve all dependencies and ensure everything is set up correctly.

### Running the Application

Run the main class, typically `Main.java` or the class containing the `public static void main(String[] args)` method.

## Usage

### Archiving Data

1. **Open the Archiver Section**

   Upon launching the application, navigate to the **Archiver** tab.

2. **Specify Data Path**

   Enter the path to the dataset you wish to archive in the provided text field.

3. **Start Archiving**

   Click on the **Archive** button to initiate the archiving process.

4. **Monitor Progress**

   - The progress bar will indicate the completion percentage.
   - Log messages will display the current status and any errors.
  
  <image src="https://github.com/user-attachments/assets/46d547da-d793-493b-8f57-d9e050c13d54" width="500" />

### Filtering Geo-Data

1. **Select Data Type**

   - Choose between **DGN** (Digital Ground Network) and **KM** (Kilometer markers) data.
   - Click on the appropriate button to proceed.

2. **Load Data (If Required)**

   If you haven't loaded the data before, you'll be prompted to select the directories containing the DGN or KM data files.

3. **Set Filtering Criteria**

   - **Location Codes**:
     - Enter the starting and ending location codes (e.g., `0000AA` to `9999ZZ` for DGN data).
   - **Kilometer Markers** (KM data only):
     - Enter the starting and ending kilometer markers (e.g., `-100` to `100`).

4. **Specify Paths**

   - **Search Path**: Directory where the application will search for data files.
   - **Save Folder**: Directory where the filtered data will be saved.

5. **Start Filtering**

   Click on the **Find Data** button to begin the filtering process.

6. **Monitor Progress**

   - The progress bar will show the filtering progress.
   - Log messages will display detailed information about the process.
  
  <image src="https://github.com/user-attachments/assets/c8650259-05e3-47c3-a4a7-c6f51de96f39" width="400" />
  <image src="https://github.com/user-attachments/assets/ee9b4174-e265-418a-9649-f8186c6d24e5" width="400" />


## How It Works

**FileArchiver** utilizes Java's powerful I/O and multi-threading capabilities to handle large datasets efficiently.

- **Archiver Module**: Scans the specified dataset directory, identifies outdated data, and archives it to optimize storage.
- **Data Loader**: Loads DGN and KM data files into memory for faster access during filtering operations.
- **Data Filter**: Applies user-defined criteria to filter out specific geo-data, which can then be used for mapping and analysis.
- **User Interface**: Built with JavaFX, the GUI provides an intuitive way to interact with the application, monitor progress, and receive feedback.

## Support

If you find FileArchiver useful and would like to support its development:

- **Bitcoin Donation**:
  - **Address**: `bc1qr8emdvwmqjl3zvrxc5v05d8sguc8vypre3ujhq`
  - You can copy the address directly from the application by clicking the **Copy BTC Address** button in the Credits section.

Your support is greatly appreciated!

## Contributing

Contributions are welcome! Here's how you can help:

1. **Report Bugs**

   - Open an issue describing the bug with steps to reproduce it.

2. **Suggest Features**

   - Open an issue with the **enhancement** label to suggest new features.

3. **Submit Pull Requests**

   - Fork the repository.
   - Create a new branch for your feature or bug fix.
   - Commit your changes with clear commit messages.
   - Push to your fork and submit a pull request.

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **Deutsche Bahn AG**: For the opportunity to develop and utilize FileArchiver.
- **Open Source Community**: For providing the tools and libraries that make this project possible.

## Contact

For any inquiries or support:

- **Developer**: Legoshi
- **GitHub**: [github.com/legoshi](https://github.com/leg0shii)
