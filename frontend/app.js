const BASE_URL = "http://localhost:8080/api/posts";

// CREATE POST
async function createPost() {
  const content = document.getElementById("postContent").value;

  const res = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      authorId: 1,
      authorType: "USER",
      content: content
    })
  });

  const data = await res.json();
  alert("Post created with ID: " + data.id);
}

// LOAD POSTS
async function loadPosts() {
  const res = await fetch(BASE_URL);
  const posts = await res.json();

  const list = document.getElementById("posts");
  list.innerHTML = "";

  posts.forEach(p => {
    const li = document.createElement("li");
    li.textContent = `ID: ${p.id} | ${p.content}`;
    list.appendChild(li);
  });
}

// ADD COMMENT
async function addComment() {
  const postId = document.getElementById("commentPostId").value;
  const content = document.getElementById("commentContent").value;
  const type = document.getElementById("authorType").value;

  const res = await fetch(`${BASE_URL}/${postId}/comments`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      authorId: Math.floor(Math.random() * 100),
      authorType: type,
      content: content,
      depthLevel: 1
    })
  });

  if (res.status === 429) {
    alert("Rate limit / cooldown hit!");
    return;
  }

  const data = await res.json();
  alert("Comment added: " + data.id);
}

// LOAD COMMENTS
async function loadComments() {
  const postId = document.getElementById("viewPostId").value;

  const res = await fetch(`${BASE_URL}/${postId}/comments`);
  const comments = await res.json();

  const list = document.getElementById("comments");
  list.innerHTML = "";

  comments.forEach(c => {
    const li = document.createElement("li");
    li.textContent = `${c.authorType}: ${c.content}`;
    list.appendChild(li);
  });
}