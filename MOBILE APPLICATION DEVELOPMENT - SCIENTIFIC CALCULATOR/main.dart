import 'package:flutter/material.dart';
import 'dart:math' as math;

void main() {
  runApp(const ScientificCalculatorApp());
}

class ScientificCalculatorApp extends StatelessWidget {
  const ScientificCalculatorApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Scientific Calculator',
      theme: ThemeData.dark().copyWith(
        scaffoldBackgroundColor: Colors.black,
      ),
      home: const CalculatorScreen(),
    );
  }
}

class CalculatorScreen extends StatefulWidget {
  const CalculatorScreen({super.key});

  @override
  State<CalculatorScreen> createState() => _CalculatorScreenState();
}

class _CalculatorScreenState extends State<CalculatorScreen> {
  String _expression = '';
  String _result = '0';

  void _onButtonPressed(String text) {
    setState(() {
      if (text == 'C') {
        _expression = '';
        _result = '0';
      } else if (text == '⌫') {
        if (_expression.isNotEmpty) {
          _expression = _expression.substring(0, _expression.length - 1);
        }
      } else if (text == '=') {
        _evaluate();
      } else {
        _expression += text;
      }
    });
  }

  void _evaluate() {
    if (_expression.isEmpty) return;
    try {
      String evalText = _expression.replaceAll('×', '*').replaceAll('÷', '/');
      
      var parser = MathParser(evalText);
      double evalResult = parser.parse();
      
      String resStr = evalResult.toString();
      if (resStr.endsWith('.0')) {
        resStr = resStr.substring(0, resStr.length - 2);
      }
      _result = resStr;
    } catch (e) {
      _result = 'Error';
    }
  }

  Widget _buildButton(String text, Color color, {double widthFactor = 1.0}) {
    return Expanded(
      flex: (widthFactor * 10).toInt(),
      child: Padding(
        padding: const EdgeInsets.all(4.0),
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(
            backgroundColor: color,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(8.0),
            ),
          ),
          onPressed: () => _onButtonPressed(text),
          child: Text(
            text,
            style: const TextStyle(
              fontSize: 20,
              color: Colors.white,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildRow(List<String> labels, Color defaultColor, {Color? lastBtnColor, List<Color?>? btnColors}) {
    return Expanded(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: List.generate(4, (index) {
          Color color = defaultColor;
          if (btnColors != null && btnColors[index] != null) {
            color = btnColors[index]!;
          } else if (index == 3 && lastBtnColor != null) {
            color = lastBtnColor;
          }
          return _buildButton(labels[index], color);
        }),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    const Color funcColor = Color(0xFF607D8B);
    const Color numColor = Color(0xFF424242);
    const Color opColor = Color(0xFFFF9800);
    const Color delColor = Color(0xFFEF5350);
    const Color calcColor = Color(0xFF4CAF50);

    return Scaffold(
      body: SafeArea(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const Padding(
              padding: EdgeInsets.all(16.0),
              child: Text(
                'Scientific Calculator',
                style: TextStyle(color: Colors.white, fontSize: 20),
              ),
            ),
            Expanded(
              flex: 1,
              child: Container(
                padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 24.0),
                alignment: Alignment.bottomRight,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.end,
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Text(
                      _expression,
                      style: const TextStyle(color: Colors.white54, fontSize: 24),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      _result,
                      style: const TextStyle(
                        color: Colors.white, 
                        fontSize: 48, 
                        fontWeight: FontWeight.bold
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const Divider(color: Colors.white24, height: 1),
            Expanded(
              flex: 2,
              child: Container(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  children: [
                    _buildRow(['sin(', 'cos(', 'tan(', 'log('], funcColor),
                    _buildRow(['sqrt(', '^', '(', ')'], funcColor),
                    _buildRow(['7', '8', '9', '÷'], numColor, lastBtnColor: opColor),
                    _buildRow(['4', '5', '6', '×'], numColor, lastBtnColor: opColor),
                    _buildRow(['1', '2', '3', '-'], numColor, lastBtnColor: opColor),
                    _buildRow(['0', '.', '⌫', '+'], numColor, 
                        btnColors: [null, null, delColor, opColor]),
                    Expanded(
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          _buildButton('C', delColor, widthFactor: 2),
                          _buildButton('=', calcColor, widthFactor: 2),
                        ],
                      ),
                    )
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class MathParser {
  final String text;
  int pos = -1;
  int ch = 0;

  MathParser(this.text);

  void nextChar() {
    pos++;
    ch = (pos < text.length) ? text.codeUnitAt(pos) : -1;
  }

  bool eat(int charToEat) {
    while (ch == 32) nextChar();
    if (ch == charToEat) {
      nextChar();
      return true;
    }
    return false;
  }

  double parse() {
    nextChar();
    double x = parseExpression();
    if (pos < text.length) throw Exception("Beklenmeyen karakter");
    return x;
  }

  double parseExpression() {
    double x = parseTerm();
    for (;;) {
      if (eat(43)) {
        x += parseTerm();
      } else if (eat(45)) {
        x -= parseTerm();
      } else {
        return x;
      }
    }
  }

  double parseTerm() {
    double x = parseFactor();
    for (;;) {
      if (eat(42)) {
        x *= parseFactor();
      } else if (eat(47)) {
        x /= parseFactor();
      } else {
        return x;
      }
    }
  }

  double parseFactor() {
    if (eat(43)) return parseFactor();
    if (eat(45)) return -parseFactor();

    double x = 0.0;
    int startPos = pos;
    if (eat(40)) { 
      x = parseExpression();
      eat(41);
    } 
    else if ((ch >= 48 && ch <= 57) || ch == 46) {
      while ((ch >= 48 && ch <= 57) || ch == 46) nextChar();
      x = double.parse(text.substring(startPos, pos));
    } 
    else if (ch >= 97 && ch <= 122) { 
      while (ch >= 97 && ch <= 122) nextChar();
      String func = text.substring(startPos, pos);
      x = parseFactor();
      if (func == "sqrt") {
        x = math.sqrt(x);
      } else if (func == "sin") {
        x = math.sin(x);
      } else if (func == "cos") {
        x = math.cos(x);
      } else if (func == "tan") {
        x = math.tan(x);
      } else if (func == "log") {
        x = math.log(x) / math.ln10; 
      } else {
        throw Exception("Bilinmeyen fonksiyon: $func");
      }
    } else {
      throw Exception("Hatalı sözdizimi");
    }

    if (eat(94)) { 
      x = math.pow(x, parseFactor()).toDouble();
    }

    return x;
  }
}
