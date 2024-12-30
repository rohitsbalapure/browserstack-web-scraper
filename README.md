# BrowserStack Web Scraper - Technical Assignment

## Overview

This repository contains a Selenium-based web scraper that fetches and processes articles from the **Opinion section** of **El País**, a Spanish news outlet. The script performs the following tasks:

1. **Web Scraping**: Scrapes article titles and content from the Opinion section.
2. **Translation**: Translates article titles from Spanish to English using a translation API.
3. **Text Processing**: Analyzes translated titles to identify repeated words.
4. **Cross-Browser Testing**: Executes the solution on BrowserStack across multiple desktop and mobile browsers.

## Features

- Scrapes titles and content from El País Opinion section.
- Downloads article cover images (if available).
- Uses Google Translate API to translate titles from Spanish to English.
- Identifies and prints repeated words in translated article titles.
- Cross-browser testing on BrowserStack with multiple browsers.

## Prerequisites

Before running the project, ensure the following:

- **Java 17+**
- **Maven** for dependency management
- **Selenium WebDriver**
- **BrowserStack Account** with access key
- **Google Translate API** or any translation API of your choice.

## Installation

### Clone the repository

```bash
git clone https://github.com/rohitsbalapure/browserstack-web-scraper.git
cd browserstack-web-scraper
