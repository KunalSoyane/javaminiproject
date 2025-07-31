document.addEventListener('DOMContentLoaded', () => {
    // This is where you would fetch data from your Java backend.
    // Example: fetch('/api/elections')
    const mockElections = [
        { 
            id: 1, 
            name: 'Student Council President', 
            description: 'Annual election for the head of the student council.',
            candidates: [
                { id: 101, name: 'Alice Johnson' },
                { id: 102, name: 'Bob Williams' }
            ]
        },
        { 
            id: 2, 
            name: 'Class Representative', 
            description: 'Choose the representative for your class.',
            candidates: [
                { id: 201, name: 'Charlie Brown' },
                { id: 202, name: 'Diana Prince' }
            ]
        },
        { 
            id: 3, 
            name: 'Sports Committee Head', 
            description: 'Elect the leader for the sports committee.',
            candidates: [
                { id: 301, name: 'Ethan Hunt' },
                { id: 302, name: 'Fiona Glenanne' }
            ]
        }
    ];

    const electionsContainer = document.getElementById('elections-container');
    const voteModalElement = document.getElementById('voteModal');
    const voteModal = new bootstrap.Modal(voteModalElement);
    const modalTitle = document.getElementById('voteModalTitle');
    const candidatesForm = document.getElementById('candidates-form');
    const submitVoteBtn = document.getElementById('submitVoteBtn');
    let selectedElectionId = null;

    // Function to display elections on the page
    function loadElections() {
        electionsContainer.innerHTML = ''; // Clear existing content
        mockElections.forEach(election => {
            const card = document.createElement('div');
            card.className = 'col-md-4 mb-4';
            card.innerHTML = `
                <div class="card election-card">
                    <div class="card-body">
                        <h5 class="card-title">${election.name}</h5>
                        <p class="card-text">${election.description}</p>
                        <button class="btn btn-success vote-btn" data-election-id="${election.id}">Vote Now</button>
                    </div>
                </div>
            `;
            electionsContainer.appendChild(card);
        });
    }
    
    // Event listener for "Vote Now" buttons
    electionsContainer.addEventListener('click', (event) => {
        if (event.target.classList.contains('vote-btn')) {
            selectedElectionId = parseInt(event.target.getAttribute('data-election-id'));
            const election = mockElections.find(e => e.id === selectedElectionId);
            
            if (election) {
                modalTitle.textContent = `Vote for: ${election.name}`;
                candidatesForm.innerHTML = ''; // Clear previous candidates
                
                election.candidates.forEach(candidate => {
                    candidatesForm.innerHTML += `
                        <label class="candidate-selection">
                            <input type="radio" name="candidate" value="${candidate.id}" required>
                            ${candidate.name}
                        </label>
                    `;
                });

                voteModal.show();
            }
        }
    });

    // Event listener for the final "Submit Vote" button in the modal
    submitVoteBtn.addEventListener('click', () => {
        const selectedCandidate = candidatesForm.querySelector('input[name="candidate"]:checked');
        
        if (!selectedCandidate) {
            alert('Please select a candidate before submitting.');
            return;
        }

        const candidateId = selectedCandidate.value;
        console.log(`Submitting vote for Election ID: ${selectedElectionId}, Candidate ID: ${candidateId}`);

        // **REAL-WORLD IMPLEMENTATION**
        // Here, you would send the data to your Java backend
        /*
        fetch('/api/votes/cast', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Include authorization token if using Spring Security with JWT
                // 'Authorization': 'Bearer ' + your_jwt_token 
            },
            body: JSON.stringify({
                electionId: selectedElectionId,
                candidateId: parseInt(candidateId)
            })
        })
        .then(response => {
            if(response.ok) {
                return response.text();
            }
            throw new Error('You have already voted in this election or an error occurred.');
        })
        .then(data => {
            alert('Vote submitted successfully!');
            voteModal.hide();
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
        });
        */

        // For demonstration purposes:
        alert(`Your vote for candidate ID ${candidateId} in election ID ${selectedElectionId} has been cast!`);
        voteModal.hide();
    });

    // Initial load of elections
    loadElections();
});