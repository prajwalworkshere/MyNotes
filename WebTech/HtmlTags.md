# HTML Tags with Syntax, Examples, and Use Cases

HTML (HyperText Markup Language) is used to create the structure of a webpage. HTML tags tell the browser how to display content such as headings, paragraphs, images, links, tables, forms, and more.

Since you're learning HTML, let's cover the important tags with their syntax, examples, and use cases.

## 1. Basic HTML document structure

Every HTML webpage generally follows this structure.

```html
<!DOCTYPE html>
<html>
<head>
    <title>My First Webpage</title>
</head>
<body>
    <h1>Hello World</h1>
    <p>This is my first webpage.</p>
</body>
</html>
```

| Tag               | Use case                                                        |
| ----------------- | --------------------------------------------------------------- |
| `<!DOCTYPE html>` | Declares the document as HTML5. It is a declaration, not a tag. |
| `<html>`          | Root element of the HTML document.                              |
| `<head>`          | Contains metadata, stylesheets, and other page information.     |
| `<title>`         | Sets the browser tab title.                                     |
| `<body>`          | Contains the visible webpage content.                           |

## 2. Heading and text tags

These tags are used to display headings, paragraphs, and formatted text.

| Tag              | Syntax                       | Use case                                                        |
| ---------------- | ---------------------------- | --------------------------------------------------------------- |
| `<h1>` to `<h6>` | `<h1>Heading</h1>`           | Creates headings from highest to lowest importance.             |
| `<p>`            | `<p>Text</p>`                | Creates a paragraph.                                            |
| `<br>`           | `Hello<br>World`             | Inserts a line break.                                           |
| `<hr>`           | `<hr>`                       | Creates a thematic break, often displayed as a horizontal line. |
| `<strong>`       | `<strong>Important</strong>` | Marks important text, usually bold.                             |
| `<b>`            | `<b>Bold</b>`                | Draws attention visually with bold text.                        |
| `<em>`           | `<em>Emphasized</em>`        | Adds emphasis, usually italic.                                  |
| `<i>`            | `<i>Italic</i>`              | Represents text in an alternate voice or style, usually italic. |
| `<u>`            | `<u>Underlined</u>`          | Marks text with an underline.                                   |
| `<mark>`         | `<mark>Highlighted</mark>`   | Highlights relevant text.                                       |
| `<small>`        | `<small>Small text</small>`  | Displays side comments or fine print.                           |
| `<del>`          | `<del>₹500</del>`            | Represents deleted text.                                        |
| `<ins>`          | `<ins>₹400</ins>`            | Represents inserted text.                                       |
| `<sub>`          | `H<sub>2</sub>O`             | Displays subscript text.                                        |
| `<sup>`          | `x<sup>2</sup>`              | Displays superscript text.                                      |
| `<pre>`          | `<pre>  Hello</pre>`         | Preserves whitespace and line breaks.                           |
| `<code>`         | `<code>print("Hi")</code>`   | Represents a piece of computer code.                            |

### Example

```html
<h1>HTML Tutorial</h1>
<h2>Introduction</h2>

<p>HTML is used to create webpages.</p>

<p><strong>Important:</strong> Practice daily.</p>

<p>Water formula: H<sub>2</sub>O</p>
<p>Square: x<sup>2</sup></p>

<hr>

<p>Learn HTML<br>Step by step</p>
```

Use case: Creating blog posts, articles, headings, educational notes, and formatted content.

## 3. Link and navigation tags

These tags help users navigate between webpages and sections.

| Tag     | Syntax                    | Use case                       |
| ------- | ------------------------- | ------------------------------ |
| `<a>`   | `<a href="URL">Visit</a>` | Creates a hyperlink.           |
| `<nav>` | `<nav>...</nav>`          | Groups major navigation links. |

### Example

```html
<a href="https://www.google.com">Visit Google</a>

<a href="about.html">About Us</a>

<a href="https://example.com" target="_blank">
    Open in New Tab
</a>

<nav>
    <a href="index.html">Home</a>
    <a href="about.html">About</a>
    <a href="contact.html">Contact</a>
</nav>
```

Use case: Website menus, navigation bars, external links, and links between pages.

Note: `target="_blank"` opens a link in a new browsing context, usually a new tab.

## 4. Image and media tags

These tags display images, audio, video, and captions.

| Tag            | Syntax                                        | Use case                                                 |
| -------------- | --------------------------------------------- | -------------------------------------------------------- |
| `<img>`        | `<img src="photo.jpg" alt="Photo">`           | Displays an image.                                       |
| `<audio>`      | `<audio controls src="song.mp3"></audio>`     | Plays audio.                                             |
| `<video>`      | `<video controls src="movie.mp4"></video>`    | Plays video.                                             |
| `<source>`     | `<source src="movie.mp4" type="video/mp4">`   | Specifies a media source.                                |
| `<figure>`     | `<figure>...</figure>`                        | Groups media with related content.                       |
| `<figcaption>` | `<figcaption>Caption</figcaption>`            | Provides a caption for a figure.                         |
| `<picture>`    | `<picture>...</picture>`                      | Allows different image sources for different conditions. |
| `<iframe>`     | `<iframe src="URL" title="Content"></iframe>` | Embeds another webpage or supported external content.    |

### Example

```html
<img src="student.jpg" alt="Student studying" width="200">

<video controls width="400">
    <source src="lesson.mp4" type="video/mp4">
    Your browser does not support video.
</video>

<audio controls>
    <source src="song.mp3" type="audio/mpeg">
</audio>

<figure>
    <img src="nature.jpg" alt="Green mountains">
    <figcaption>Beautiful mountains</figcaption>
</figure>
```

Use case: Photo galleries, educational websites, video lessons, music players, and embedded maps.

## 5. List tags

HTML provides ordered lists, unordered lists, and description lists.

| Tag    | Syntax                     | Use case                              |
| ------ | -------------------------- | ------------------------------------- |
| `<ul>` | `<ul>...</ul>`             | Creates a bulleted list.              |
| `<ol>` | `<ol>...</ol>`             | Creates a numbered list.              |
| `<li>` | `<li>Item</li>`            | Defines a list item.                  |
| `<dl>` | `<dl>...</dl>`             | Creates a description list.           |
| `<dt>` | `<dt>HTML</dt>`            | Defines a term in a description list. |
| `<dd>` | `<dd>Markup language</dd>` | Describes a term.                     |

### Example

```html
<h3>Programming Languages</h3>

<ul>
    <li>Java</li>
    <li>C++</li>
    <li>Python</li>
</ul>

<h3>Steps to Learn HTML</h3>

<ol>
    <li>Learn basic tags</li>
    <li>Learn forms</li>
    <li>Build a webpage</li>
</ol>

<dl>
    <dt>HTML</dt>
    <dd>Defines webpage structure.</dd>

    <dt>CSS</dt>
    <dd>Styles the webpage.</dd>
</dl>
```

Use case: Shopping lists, tutorials, menus, instructions, and glossaries.

## 6. Table tags

Tables organize information into rows and columns.

| Tag          | Syntax                            | Use case                           |
| ------------ | --------------------------------- | ---------------------------------- |
| `<table>`    | `<table>...</table>`              | Creates a table.                   |
| `<tr>`       | `<tr>...</tr>`                    | Creates a row.                     |
| `<th>`       | `<th>Name</th>`                   | Creates a header cell.             |
| `<td>`       | `<td>Prajwal</td>`                | Creates a data cell.               |
| `<thead>`    | `<thead>...</thead>`              | Groups the header rows.            |
| `<tbody>`    | `<tbody>...</tbody>`              | Groups the main data rows.         |
| `<tfoot>`    | `<tfoot>...</tfoot>`              | Groups footer or summary rows.     |
| `<caption>`  | `<caption>Student Data</caption>` | Adds a table caption.              |
| `<colgroup>` | `<colgroup>...</colgroup>`        | Groups columns for formatting.     |
| `<col>`      | `<col>`                           | Specifies properties for a column. |

### Example

```html
<table border="1">
    <caption>Student Marks</caption>

    <thead>
        <tr>
            <th>Name</th>
            <th>Subject</th>
            <th>Marks</th>
        </tr>
    </thead>

    <tbody>
        <tr>
            <td>Amit</td>
            <td>HTML</td>
            <td>85</td>
        </tr>

        <tr>
            <td>Priya</td>
            <td>CSS</td>
            <td>90</td>
        </tr>
    </tbody>
</table>
```

Use case: Student records, product prices, employee data, and reports.

For modern webpages, CSS is generally preferred over the `border` attribute for table styling.

## 7. Form tags

Forms collect information from users, such as names, email addresses, passwords, and feedback.

| Tag          | Syntax                                            | Use case                                     |
| ------------ | ------------------------------------------------- | -------------------------------------------- |
| `<form>`     | `<form action="/submit" method="post">...</form>` | Groups controls for submitting data.         |
| `<label>`    | `<label for="name">Name</label>`                  | Provides a descriptive label for an input.   |
| `<input>`    | `<input type="text">`                             | Accepts user input.                          |
| `<textarea>` | `<textarea></textarea>`                           | Accepts multiline text.                      |
| `<button>`   | `<button type="submit">Submit</button>`           | Creates a clickable button.                  |
| `<select>`   | `<select>...</select>`                            | Creates a dropdown menu.                     |
| `<option>`   | `<option>India</option>`                          | Defines a dropdown choice.                   |
| `<optgroup>` | `<optgroup label="Asia">...</optgroup>`           | Groups related dropdown options.             |
| `<fieldset>` | `<fieldset>...</fieldset>`                        | Groups related form controls.                |
| `<legend>`   | `<legend>Personal Details</legend>`               | Gives a caption to a fieldset.               |
| `<datalist>` | `<datalist id="cities">...</datalist>`            | Provides suggestions for an input.           |
| `<output>`   | `<output>100</output>`                            | Displays a calculated result.                |
| `<progress>` | `<progress value="60" max="100"></progress>`      | Displays task progress.                      |
| `<meter>`    | `<meter value="7" min="0" max="10"></meter>`      | Displays a measurement within a known range. |

### Example: Registration form

```html
<form action="/register" method="post">

    <label for="name">Full Name:</label>
    <input type="text" id="name" name="name" required>

    <br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required>

    <br><br>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password">

    <br><br>

    <label for="course">Course:</label>
    <select id="course" name="course">
        <option value="html">HTML</option>
        <option value="css">CSS</option>
        <option value="js">JavaScript</option>
    </select>

    <br><br>

    <label for="message">Message:</label>
    <textarea id="message" name="message"></textarea>

    <br><br>

    <button type="submit">Register</button>

</form>
```

Use case: Login pages, registration forms, contact forms, surveys, and search forms.

Important: HTML creates the form structure. Processing and storing submitted information generally requires a backend or another configured service.

## 8. Common input types

The `<input>` tag supports different types depending on the data being collected.

| Input type | Syntax                                | Use case                                                 |
| ---------- | ------------------------------------- | -------------------------------------------------------- |
| `text`     | `<input type="text">`                 | Names and short text.                                    |
| `password` | `<input type="password">`             | Password entry with masked display.                      |
| `email`    | `<input type="email">`                | Email address entry.                                     |
| `number`   | `<input type="number">`               | Numeric input.                                           |
| `tel`      | `<input type="tel">`                  | Telephone number entry.                                  |
| `date`     | `<input type="date">`                 | Date selection.                                          |
| `time`     | `<input type="time">`                 | Time selection.                                          |
| `radio`    | `<input type="radio">`                | Select one option in a group.                            |
| `checkbox` | `<input type="checkbox">`             | Select multiple independent options.                     |
| `file`     | `<input type="file">`                 | Upload a file.                                           |
| `range`    | `<input type="range">`                | Select a value using a slider.                           |
| `color`    | `<input type="color">`                | Select a color.                                          |
| `search`   | `<input type="search">`               | Enter search terms.                                      |
| `url`      | `<input type="url">`                  | Enter a web address.                                     |
| `hidden`   | `<input type="hidden">`               | Submit a value that isn't displayed as a normal control. |
| `submit`   | `<input type="submit" value="Send">`  | Submit a form.                                           |
| `reset`    | `<input type="reset" value="Reset">`  | Reset form controls to their initial values.             |
| `button`   | `<input type="button" value="Click">` | Creates a button without built-in submission behavior.   |

Example:

```html
<form>
    <p>Select your gender:</p>

    <input type="radio" id="male" name="gender" value="male">
    <label for="male">Male</label>

    <input type="radio" id="female" name="gender" value="female">
    <label for="female">Female</label>

    <p>Select your skills:</p>

    <input type="checkbox" id="html" name="skills" value="html">
    <label for="html">HTML</label>

    <input type="checkbox" id="css" name="skills" value="css">
    <label for="css">CSS</label>
</form>
```

## 9. Semantic HTML tags

Semantic tags describe the meaning and purpose of webpage content. They improve readability, accessibility, and document structure.

`<header>` — Page or section header

Syntax: `<header>...</header>`

Use case: Website title, logo, introductory content.

[Common UI Components course lesson | Uxcel](https://images.openai.com/static-rsc-4/tY3izLq9fLJhlwvme_MeBxXDoYGVBL-NVpJFBObTSMxX_lmL44xpNDOjF5gW3Fz5P-PeuVTJu3IWp-6qpuJOIyzgxkDuwv5RetJTdhJvgPwzQ9dv3v31n_M-tJNw_pr0yZCaFyIDi6PcnR8YXlxUrMR-t58jzGTg_DPOz41gC_s?purpose=inline)

`<nav>` — Navigation area

Syntax: `<nav>...</nav>`

Use case: Main menu links and navigation.

[Business Blog Article Page UI – Clean Editorial Layout by Creliq UX/UI Design Agency on Dribbble](https://images.openai.com/static-rsc-4/mCg54N8BBhUTrZhISy8UZp1gay_hJFdc-wWGmXyH2sANDDlC-e2rZT4V3ZHsksm8rAB2xdBqJJEPvfYounnD1gt4MLGufNvj4-AAy6NfJJYaJyrOSoBEddqRaeAf1udEbmCI9BGuGrbAEH0zMv-g_5Q3iDGWlOkyMKJTB5PPYGQ?purpose=inline)

`<main>` — Main content

Syntax: `<main>...</main>`

Use case: The central content unique to the page.

[Modern Editorial News Site by Iqbal Tahir on Dribbble](https://images.openai.com/static-rsc-4/LEruSCCTCkcUj3XHqQEJxfnhP-OZSFgeALZtGxinNxm3g4duCuB_S1ihjVfu4s_8K-4SDH7k8WfefoCLjAvLNwxhQndMhFz2xWl4zaGOi-CWHFZAVvE4GYLuzqBCRonkqWfRtp2ELr24svCep-qKP2l84Fqs3i2Aw2-Vvy2V7xw?purpose=inline)

`<section>` — Thematic section

Syntax: `<section>...</section>`

Use case: Grouping related content under a heading.

[Shadcn Blog Cards Examples - Shadcnuikit.com](https://images.openai.com/static-rsc-4/FzjNY2w0ry3c6t44M1M4hbTWIiR8G4bV1_xv9Hlte02bW8-78jkAS7IWWP9VJqZSXpRLAK4cBMKSSu_E4Xqw-HD5I9mio_ZL7SlUpeCRdvkg3fCV2hbKZyrEkELjvXyaZ-aX3uOA_9Dcv_6BcHmog3jcaHVAO4RHPUzg1zKOVLw?purpose=inline)

`<article>` — Independent content

Syntax: `<article>...</article>`

Use case: Blog posts, news articles, forum posts.

[Sidebar Recommended Content – Grow](https://images.openai.com/static-rsc-4/3yj4L6sEyoPeu8SBjYhBbWpYwb_Oq7HyIQg3_l8hVEudhLkutDfShz7QcwVnohUfzhm2A9-SWOzgtCZDbBFQx333v6LtgJsIm8ylG2-A9M0z5OC3DSXgR6PuBohFnrvDJNP6oUiB-0CdIbNwZ_BkKqYv76k3SoPwT1dVwGwQVJY?purpose=inline)

`<aside>` — Related or supplementary content

Syntax: `<aside>...</aside>`

Use case: Sidebars, related links, supplementary notes.

[Footer for landing page/website :: Behance](https://images.openai.com/static-rsc-4/YzB_f_HWCtkuoEDwlGg8_WbKFnO-pD0EnnUUrkH-coebUcrjtfbwBvz38gINGCPFhlfCiOHbf_e4aWbGvVQqKD72gvgyzoX6B3SPuU-86TFJ5qmW-ieXPCL6aQgxAm0EQbmc7o46rbsozzFizYyAdwj8CtTveTJqgTJ4Buvj-UU?purpose=inline)

`<footer>` — Footer area

Syntax: `<footer>...</footer>`

Use case: Copyright, contact information, footer links.

Other semantic tags:

| Tag            | Use case                                                   |
| -------------- | ---------------------------------------------------------- |
| `<details>`    | Creates expandable and collapsible content.                |
| `<summary>`    | Provides the visible heading for a `<details>` element.    |
| `<time>`       | Represents a date or time.                                 |
| `<address>`    | Provides contact information for a person or organization. |
| `<blockquote>` | Represents a longer quotation.                             |
| `<q>`          | Represents a short inline quotation.                       |
| `<abbr>`       | Represents an abbreviation.                                |
| `<cite>`       | Identifies the title of a creative work.                   |

Example:

```html
<header>
    <h1>My Blog</h1>
</header>

<nav>
    <a href="index.html">Home</a>
    <a href="articles.html">Articles</a>
</nav>

<main>
    <article>
        <h2>Learning HTML</h2>
        <p>HTML structures webpages.</p>
    </article>

    <aside>
        <p>Related topic: CSS</p>
    </aside>
</main>

<footer>
    <p>Copyright 2026 My Blog</p>
</footer>
```

## 10. Container and grouping tags

| Tag      | Syntax             | Use case                                          |
| -------- | ------------------ | ------------------------------------------------- |
| `<div>`  | `<div>...</div>`   | Groups block-level content for layout or styling. |
| `<span>` | `<span>...</span>` | Groups inline text or content for styling.        |

Example:

```html
<div class="card">
    <h2>Student Details</h2>
    <p>Name: Prajwal</p>
    <p>Status: <span class="active">Active</span></p>
</div>
```

- `div` is generally used to group larger sections or components.
- `span` is generally used to target a small part of text.

Both are non-semantic elements. Use semantic tags when they describe the content more accurately.

## 11. HTML and CSS/JavaScript supporting tags

| Tag          | Syntax                                     | Use case                                                             |
| ------------ | ------------------------------------------ | -------------------------------------------------------------------- |
| `<link>`     | `<link rel="stylesheet" href="style.css">` | Links external resources, commonly CSS files.                        |
| `<style>`    | `<style>p { color: red; }</style>`         | Adds CSS inside an HTML document.                                    |
| `<script>`   | `<script src="app.js"></script>`           | Loads or contains JavaScript.                                        |
| `<meta>`     | `<meta charset="UTF-8">`                   | Defines metadata, such as character encoding.                        |
| `<base>`     | `<base href="https://example.com/">`       | Sets the base URL for relative URLs.                                 |
| `<template>` | `<template>...</template>`                 | Holds HTML that is not rendered until used by scripts.               |
| `<noscript>` | `<noscript>Enable JavaScript.</noscript>`  | Displays fallback content when scripting is unavailable or disabled. |

Example:

```html
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Website</title>

    <link rel="stylesheet" href="style.css">
</head>

<body>
    <h1>Hello</h1>

    <script src="app.js"></script>
</body>
```

## 12. Other useful HTML tags

| Tag          | Syntax                                       | Use case                                                      |
| ------------ | -------------------------------------------- | ------------------------------------------------------------- |
| `<canvas>`   | `<canvas id="myCanvas"></canvas>`            | Provides a drawing surface controlled by JavaScript.          |
| `<svg>`      | `<svg>...</svg>`                             | Embeds scalable vector graphics.                              |
| `<dialog>`   | `<dialog open>Hello</dialog>`                | Represents a dialog box.                                      |
| `<progress>` | `<progress value="30" max="100"></progress>` | Displays task completion progress.                            |
| `<meter>`    | `<meter value="5" min="0" max="10"></meter>` | Displays a scalar measurement within a range.                 |
| `<wbr>`      | `LongWord<wbr>AnotherPart`                   | Suggests a possible line-break position.                      |
| `<bdi>`      | `<bdi>مرحبا</bdi>`                           | Isolates text whose writing direction may differ.             |
| `<bdo>`      | `<bdo dir="rtl">Hello</bdo>`                 | Overrides text direction.                                     |
| `<ruby>`     | `<ruby>漢<rt>かん</rt></ruby>`                  | Adds pronunciation annotations, commonly for East Asian text. |
| `<data>`     | `<data value="101">Product A</data>`         | Associates visible content with a machine-readable value.     |

## 13. Void tags vs. normal tags

This is an important HTML concept for exams and interviews.

Void elements do not have closing tags and cannot contain child elements.

Examples:

```html
<img src="photo.jpg" alt="Photo">
<br>
<hr>
<input type="text">
<meta charset="UTF-8">
<link rel="stylesheet" href="style.css">
```

Normal elements generally have opening and closing tags.

```html
<p>Hello World</p>
<div>Content</div>
<h1>Heading</h1>
<button>Click Me</button>
```

| Void elements                  | Normal elements                       |
| ------------------------------ | ------------------------------------- |
| No closing tag                 | Usually have opening and closing tags |
| Cannot contain child elements  | Can contain content or other elements |
| Examples: `img`, `br`, `input` | Examples: `p`, `div`, `section`       |

## 14. Complete example combining multiple tags

This example combines headings, paragraphs, links, lists, tables, forms, and semantic elements into one webpage.

```html


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Portal</title>
</head>
<body>

    <header>
        <h1>Student Portal</h1>
        <nav>
            <a href="#about">About</a>
            <a href="#courses">Courses</a>
            <a href="#contact">Contact</a>
        </nav>
    </header>

    <main>
        <section id="about">
            <h2>About Us</h2>
            <p>Welcome to our student portal.</p>
            <img src="student.jpg" alt="Student studying" width="150">
        </section>

        <section id="courses">
            <h2>Available Courses</h2>
            <ul>
                <li>HTML</li>
                <li>CSS</li>
                <li>JavaScript</li>
            </ul>

            <table border="1">
                <tr>
                    <th>Course</th>
                    <th>Duration</th>
                </tr>
                <tr>
                    <td>HTML</td>
                    <td>2 Weeks</td>
                </tr>
                <tr>
                    <td>CSS</td>
                    <td>3 Weeks</td>
                </tr>
            </table>
        </section>

        <section id="contact">
            <h2>Contact Us</h2>

            <form>
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" required>

                <br><br>

                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>

                <br><br>

                <button type="submit">Send</button>
            </form>
        </section>
    </main>

    <footer>
        <p>Copyright 2026 Student Portal</p>
    </footer>

</body>
</html>

```

## 15. Quick revision for viva

| Question                                             | Answer                                                                                                       |
| ---------------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| What is an HTML tag?                                 | A markup construct used to define an element or structure in HTML.                                           |
| What is an HTML element?                             | The complete structure, usually including the opening tag, content, and closing tag.                         |
| What is an attribute?                                | Additional information or configuration written in an element's opening tag.                                 |
| What is the difference between `<div>` and `<span>`? | `div` is generally used for block-level grouping; `span` for inline grouping.                                |
| What is the difference between `<b>` and `<strong>`? | `b` draws attention stylistically; `strong` indicates importance.                                            |
| What is the difference between `<i>` and `<em>`?     | `i` represents alternate voice or style; `em` indicates emphasis.                                            |
| What is a semantic tag?                              | A tag that conveys the meaning or purpose of its content.                                                    |
| What is a void element?                              | An element that cannot have child content or a closing tag, such as `img`.                                   |
| What is the use of `alt`?                            | Provides alternative text for an image, especially for accessibility and when the image cannot be displayed. |
| What is the use of `href`?                           | Specifies the destination of a hyperlink or linked resource, depending on the element.                       |
| What is the use of `src`?                            | Specifies the source URL of a resource, such as an image or script.                                          |
| What is the use of `id`?                             | Gives an element a unique identifier within the document.                                                    |
| What is the use of `class`?                          | Groups elements for CSS styling or JavaScript selection.                                                     |

Recommended learning order: Basic structure → text tags → links and images → lists → tables → forms → semantic tags → CSS and JavaScript integration.
