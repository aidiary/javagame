#!/usr/bin/perl

# 送信されたデータを受け取る
if ($ENV{'REQUEST_METHOD'} eq "POST") {
	read(STDIN, $buffer, $ENV{'CONTENT_LENGTH'});
}

# データを分割する
@pairs = split(/&/, $buffer);
# 各データをさらに分割して%paramハッシュに入れる
foreach $pair (@pairs) {
	($key, $value) = split(/=/, $pair);
	# valueが日本語の場合コード化されているので変換する
	$value =~ s/%([a-fA-F0-9][a-fA-F-0-9])/pack("C", hex($1))/eg;
	$param{$key} = $value;
}

# 出力されるHTML
print <<END;
Content-type: text/html

<html>
<head><title>あなたの得点は登録されました</title></head>
<body>
<h1>あなたの得点は登録されました</h1>
<p>$param{name}さんは$param{score}点です。</p>
</body>
</html>
END
