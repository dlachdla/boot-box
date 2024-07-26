//0414
function setMovieCookieAndRedirect(cookieName, movieId, destination) {
    // 쿠키 저장
    document.cookie = cookieName + '=' + movieId + '; path=/';
    // 페이지 이동
    window.location.href = destination;
}
$(document).ready(function() {
    let page = 0; // 현재 페이지 번호
    const size = 10; // 페이지당 영화 수

    // 페이지 로드와 동시에 첫 페이지의 영화 목록 로드
    loadMovies(page, size, '');

    $('#btn-page').on('click', function() {
        loadMovies(page, size, $('#underline_select').val());
    });

    // 장르 선택 시 영화 목록 업데이트
    $('#underline_select').change(function() {
        page = 0; // 페이지 번호를 0으로 리셋
        selectedGenre = $(this).val(); // 선택된 장르 업데이트
        console.log(selectedGenre);
        $('.section-1-2').empty(); // 기존 영화 목록 비우기
        loadMovies(page, size, selectedGenre); // 새로운 장르의 첫 페이지로 영화 목록 로드
    });

    function getRatingSpan(movie) {
        let ratingClass = '';
        let ratingText = '';

        switch (movie.filmRatings) {
            case '전체관람가':
                ratingClass = 'grade bg-gradient-to-r bg-green-100 text-green-800 text-m font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-green-400 border border-green-400';
                ratingText = 'ALL';
                break;
            case '12세관람가':
                ratingClass = 'grade bg-gradient-to-r bg-yellow-100 text-yellow-800 text-l font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-yellow-300 border border-yellow-300';
                ratingText = '12';
                break;
            case '12세이상관람가':
                ratingClass = 'grade bg-gradient-to-r bg-yellow-100 text-yellow-800 text-l font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-yellow-300 border border-yellow-300';
                ratingText = '12';
                break;
            case '15세관람가':
                ratingClass = 'grade bg-gradient-to-r bg-purple-100 text-purple-800 text-l font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-purple-400 border border-purple-400';
                ratingText = '15';
                break;
            case '15세이상관람가':
                ratingClass = 'grade bg-gradient-to-r bg-purple-100 text-purple-800 text-l font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-purple-400 border border-purple-400';
                ratingText = '15';
                break;
                //관람등급에 고등학생가 라는 것도 있는데, 서치해보니 15세와 동일하다고 하네요.
            case '고등학생가':
                ratingClass = 'grade bg-gradient-to-r bg-purple-100 text-purple-800 text-l font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-purple-400 border border-purple-400';
                ratingText = '15';
                break;
            case '18세관람가(청소년관람불가)':
                ratingClass = 'grade bg-gradient-to-r bg-red-100 text-red-800 text-l font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-red-400 border border-red-400';
                ratingText = '18+';
                break;
            case '청소년관람불가':
                ratingClass = 'grade bg-gradient-to-r bg-red-100 text-red-800 text-l font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-red-400 border border-red-400';
                ratingText = '18+';
                break;
            default:
                ratingClass = 'grade bg-gradient-to-r shadow-lg shadow-gray-100/50 bg-gray-100 text-gray-800 text-l font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-gray-400 border border-gray-500';
                ratingText = '미정';
        }

        return `<span style="font-size: 0.2em" class="grade bg-gradient-to-r ${ratingClass} text-m font-bold me-2 px-2.5 py-0.5 rounded dark:bg-gray-700 dark:text-green-400 border border-green-400">${ratingText}</span>`;
    }

    // 제목을 줄이는 함수
    function abbreviateTitle(title, maxLength) {
        if (title.length <= maxLength) {
            return title;
        } else {
            return title.substring(0, maxLength - 3) + '...'; // 끝을 '...'으로 표시
        }
    }

    // 영화 상세 페이지로의 링크 생성 함수
    function getMovieDetailLink(movie) {
        const abbreviatedTitle = abbreviateTitle(movie.title, 20); // 20자로 제목을 줄임
        return `<a class="movie-title mb-1" href="${contextPath}movie/movieDetail.do?id=${movie.id}">${abbreviatedTitle}</a>`;
    }


    function getMovieHtml(movie) {
        let posterSrc = movie.posterUrl ? (movie.posterUrl.startsWith('http://file.koreafilm.or.kr') ? movie.posterUrl : `https://image.tmdb.org/t/p/w200${movie.posterUrl}`) : 'https://t4.ftcdn.net/jpg/03/08/67/51/360_F_308675145_Ye70fJFVPntNVnmxjtVgMy5P8MDEmusB.jpg';
        let ratingSpan = getRatingSpan(movie);
        let title = getMovieDetailLink(movie);
        let rankDisplay = movie.rank ? `<strong class="rank text-red-500 mr-2 top-0 right-0">No.${movie.rank}</strong>` : '';

        return `
        <div class="movie-card">
            <div>
                ${rankDisplay}
                
                <a href="${contextPath}movie/movieDetail.do?id=${movie.id}">
                    ${ratingSpan}
                    <img loading="lazy" src="${posterSrc}" alt="${movie.title}">
                </a>
            </div>
            
            ${title}
            
            <div class="m-0 p-0">
                <strong class="text-xs text-zinc-400">
                    평점
                    <span class="text-xs text-zinc-400 mx-1">${movie.voteAverage}</span>
                    |
                </strong>

                <strong class="text-xs text-zinc-400 mx-1">
                    예매율
                    <span class="text-xs text-zinc-400">${movie.cumulativeBookingRate.toFixed(1)}%</span>
                </strong>
            </div>
            <span class="text-xs text-zinc-400">
                <strong>${new Date(movie.releaseDate).toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' })} 개봉</strong>
            </span>
            ${movie.dday !== null && movie.dday !== undefined ? `<em class="text-base text-red-500 ml-2">${movie.dday}</em>` : ''}
            <div class="movie_action">
                <button class="movie_button text-sm mb-3" 
                onclick="setMovieCookieAndRedirect('movieIdCookie', ${movie.id}, '${contextPath}reservation/reservationBooking.do')">
                예매하기</button>
            </div>
        </div>
    `;
    }

    function loadMovies(currentPage, pageSize, genre) {
        $.ajax({
            url: `${contextPath}movie/moviePageList.do`,
            type: 'GET',
            data: {
                page: currentPage,
                size: pageSize,
                genre: genre
            },
            success: function(response) {
                if(currentPage === 0) $('.section-1-2').empty(); // 영화 목록 비우기
                // 서버로부터 받은 데이터로 HTML 컨텐츠 생성 및 추가
                response.content.forEach(function(movie) {
                    const movieHtml = getMovieHtml(movie);
                    $('.section-1-2').append(movieHtml);
                });

                // 페이지 변수 업데이트 및 UI 업데이트
                page = response.currentPage + 1;
                $('#page').text(page); // 현재 페이지 번호 업데이트
                $('#totalPage').text(response.totalPages); // 총 페이지 수 업데이트

                // 마지막 페이지에 도달했으면 더보기 버튼 숨김
                if (!response.hasNext || page >= response.totalPages) {
                    $('#btn-page').hide();
                } else {
                    $('#btn-page').show(); // 다음 페이지가 있으면 더보기 버튼 다시 표시
                }
            },
            error: function(error) {
                console.error('영화 목록을 불러오는 데 실패했습니다.', error);
            }
        });
    }
});